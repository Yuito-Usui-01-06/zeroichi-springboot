package com.zeroichi.app.controller;

import com.zeroichi.app.domain.File;
import com.zeroichi.app.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @PostMapping
    public ResponseEntity<File> createFile(@RequestBody File file) {
        File savedFile = fileRepository.save(file);
        return ResponseEntity.ok(savedFile);
    }
}