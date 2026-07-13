package com.festi_ci.parti_ci.fileManager;

import com.festi_ci.parti_ci.config.MinioProperties;
import com.festi_ci.parti_ci.user.UserModel;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
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
    String storedName = filePath + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
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

    public String getPresignedUrl(String id) throws Exception {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        // Generate presigned URL to download object with GET method valid 1 hour
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(file.getStoredName())
                        .method(Method.GET) // GET, PUT, DELETE, HEAD
                        .expiry(1, TimeUnit.HOURS)
                        .build()
        );
    }

    public List<FileModel> getAll(){
        return fileRepository.findAll();
    }


    public void ensureBucketExists() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if(!bucketExists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
        }
    }
}