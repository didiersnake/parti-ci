package com.festi_ci.parti_ci.fileManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Service
public class FileValidationService {

    private final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private final List<String> ALLOWED_MIMETYPE = List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    public void validateFile(MultipartFile file){
        try{
            if(file.isEmpty()) throw new RuntimeException("File is required");
            if(!isAllowedSize(file)) throw new RuntimeException("File size is too large");
            if(!isAllowedExtension(file)) throw new RuntimeException("File extension is not allowed");
            if(!isAllowedMimeType(file)) throw new RuntimeException("File type is not allowed");
            if(!isValidImageMagicBytes(file)) throw new RuntimeException("File is not a valid image");
        }catch (Exception e){
            log.error("Failed to validate file {} : {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("Failed to validate file", e);
        }
    }

    private boolean isAllowedSize(MultipartFile file) {
        long MAX_FILE_SIZE = 1024 * 1024; // 1MB
        return file.getSize() <= MAX_FILE_SIZE;
    }

    private boolean isAllowedExtension(MultipartFile file) {
        String extension = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf("."));
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private boolean isAllowedMimeType(MultipartFile file) {
        String mimeType = file.getContentType();
        return ALLOWED_MIMETYPE.contains(mimeType);
    }

    /**
     * Checks if the file is a valid image by reading the first 12 bytes and checking the image signature.
     * Note : Magic Bytes (File Signatures) are the first few bytes of a file that identify its actual type, regardless of extension.
     *        0xFF (Java hexadecimal representation of FF) is 255 in decimal
     *        0x is java prefix to represent hexadecimal
     *        Each file type has a unique signature ( FF D8 FF for JPEG)
     *        (byte) is used to cast the hexadecimal to a primitive byte for comparing with header bytes
     * @param file
     * @return boolean
     * @throws IOException
     */
    private boolean isValidImageMagicBytes(MultipartFile file) throws IOException {
        byte[] header = new byte[12]; // Read first 12 bytes
        file.getInputStream().read(header);

        // JPEG: FF D8 FF
        if (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF) {
            return true;
        }
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if (header[0] == (byte) 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) {
            return true;
        }
        // GIF: 47 49 46 38
        if (header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x38) {
            return true;
        }
        // WebP: RIFF...WEBP
        if (header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46
                && header[8] == 0x57 && header[9] == 0x45 && header[10] == 0x42 && header[11] == 0x50) {
            return true;
        }
        return false;
    }
}