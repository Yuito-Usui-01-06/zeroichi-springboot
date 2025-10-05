package com.zeroichi.app.controller;

import com.zeroichi.app.domain.File;
import com.zeroichi.app.repository.FileRepository;
import com.zeroichi.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    // 新規ファイル作成エンドポイント
    @PostMapping
    public ResponseEntity<File> createFile(@RequestBody File file) {
        if (file.getUserId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        file.setCreatedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());
        File savedFile = fileRepository.save(file);
        return ResponseEntity.ok(savedFile);
    }

    // ユーザーIDからファイルを取得するエンドポイント
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<File>> getFilesByUserId(@PathVariable Long userId) { // String → Long
        List<File> files = fileRepository.findByUserId(userId);
        return ResponseEntity.ok(files);
    }

    // ファイル名更新エンドポイント
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFileName(@PathVariable Long id, @RequestBody File updatedFile) { // String → Long
        return fileRepository.findById(id)
                .map(file -> {
                    file.setName(updatedFile.getName());
                    file.setUpdatedAt(LocalDateTime.now());
                    fileRepository.save(file);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 特定のファイルIDからファイルを取得するエンドポイント
    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) { // String → Long
        return fileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            fileRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}