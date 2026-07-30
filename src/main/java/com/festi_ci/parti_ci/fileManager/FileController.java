package com.festi_ci.parti_ci.fileManager;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class FileController {
    private final MiniOService miniOService;

    public FileController(MiniOService miniOService) {
        this.miniOService = miniOService;
    }

    @GetMapping
    public ResponseEntity<List<FileModel>> getAllFiles() {
        return ResponseEntity.ok(miniOService.getAll());
    }

    @GetMapping("/covers/{category}")
    public ResponseEntity<?> getImageCoverByCategory(@PathVariable String category) throws Exception {
        log.info("Getting image cover by category: {}", category);
        return ResponseEntity.status(HttpStatus.OK).body(miniOService.getCoversByCategory(category));
    }
}