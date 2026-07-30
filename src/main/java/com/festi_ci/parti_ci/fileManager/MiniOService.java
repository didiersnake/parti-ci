package com.festi_ci.parti_ci.fileManager;

import com.festi_ci.parti_ci.config.MinioProperties;
import com.festi_ci.parti_ci.user.UserModel;
import com.festi_ci.parti_ci.utils.StringUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MiniOService{
    private final MinioProperties minioProperties;
    private final MinioClient minioClient;
    private final FileRepository fileRepository;

    @Autowired
    public MiniOService(MinioProperties minioProperties, FileRepository fileRepository) {
        this.minioProperties = minioProperties;
        this.fileRepository = fileRepository;
        this.minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint()).credentials(
                        minioProperties.getAccessKey(),
                        minioProperties.getSecretKey()
                ).build();
    }

    public FileModel uploadFile(MultipartFile file, String filePath, Long uploadedBy) throws Exception {
    ensureBucketExists();
    String sanitizedFileName = StringUtils.slugify(file.getOriginalFilename())
            .replaceAll("[^a-zA-Z0-0._-]", ""); // Remove special characters
    String storedName = filePath + "/" + UUID.randomUUID() + "-" + sanitizedFileName;
    // Upload file to MinIO { bucketName, storedName, file }
        try(InputStream fileInputStream = file.getInputStream()){
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(storedName)
                            .stream(fileInputStream, file.getSize(), -1)
                            .build()
            );
        }
    // Save metadata to database
        FileModel fileData = FileModel.builder().fileName(file.getOriginalFilename())
                .uploadedBy(uploadedBy).storedName(storedName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
        return fileRepository.save(fileData);
    }

    /**
     * Get presigned URL for file
     * @param id file id from the file metadata
     * @param method "get" or "put"
     * @return
     * @throws Exception
     */
    public String getPresignedUrl(String id, String method) throws Exception {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        // Generate presigned URL to download object with GET method valid 1 hour
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("response-content-type", "application/json");
        return switch (method) {
            case "get" -> minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(file.getStoredName())
                            .method(Method.GET) // GET, PUT, DELETE, HEAD
                            .expiry(1, TimeUnit.HOURS)
                            .extraQueryParams(reqParams)
                            .build()
            );
            case "put" -> minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(file.getStoredName())
                            .method(Method.PUT) // GET, PUT, DELETE, HEAD
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
            default -> null;
        };
    }

    /**
     * Get image cover by category
     * @param category
     * @return
     */
    public List<String> getImageCoverUrlByCategory(String category){
//        log.info("Getting image cover from bucket: {}", minioProperties.getBucketName());
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .prefix("event-covers/" + category + "/")
                        .recursive(true).build());
        // Consume the lazy iterable to get actual data
        List<String> items = new ArrayList<>();
        for (Result<Item> result : results) {
            try {
//                items.add(result.get().objectName());
                Item item = result.get();
                items.add(item.objectName());
            } catch (Exception e) {
                log.error("Error listing objects", e);
            }
        }
        return items;
    }

    public List<String> getCoversByCategory(String category) throws Exception {
        List<String> urls = new ArrayList<>();
        List<String> objectNames = getImageCoverUrlByCategory(category); // your existing method
        for (String objectName : objectNames) {
            // Generate presigned URL for each object
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
            urls.add(url);
        }
        return urls;
    }

    public List<FileModel> getAll(){
        return fileRepository.findAll();
    }

    public String getCoverUrl(String category) throws Exception{
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(category)
                        .method(Method.GET)
                        .expiry(1, TimeUnit.HOURS)
                        .build()
        );
    }


    public void ensureBucketExists() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if(!bucketExists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
        }
    }
}