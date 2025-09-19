package com.zeroichi.app.controller;

import com.zeroichi.app.domain.File;
import com.zeroichi.app.domain.User;
import com.zeroichi.app.repository.FileRepository;
import com.zeroichi.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername())
                .map(foundUser -> {
                    if (foundUser.getPassword().equals(user.getPassword())) {

                        // 💡 ログイン成功後、ユーザーに紐づく既存のファイルを検索
                        List<File> existingFiles = fileRepository.findByUserId(foundUser.getId());
                        File fileToReturn;

                        if (existingFiles.isEmpty()) {
                            // 💡 既存ファイルがない場合のみ、新しいファイルを作成
                            File newFile = new File();
                            newFile.setName("新規ホワイトボード");
                            newFile.setUserId(foundUser.getId());
                            newFile.setCreatedAt(LocalDateTime.now());
                            newFile.setUpdatedAt(LocalDateTime.now());
                            fileToReturn = fileRepository.save(newFile);
                        } else {
                            // 💡 既存ファイルがある場合、最初のファイルを使用
                            fileToReturn = existingFiles.get(0);
                        }

                        // ユーザーIDと返却するファイルIDをレスポンスとして返す
                        Map<String, String> response = new HashMap<>();
                        response.put("userId", foundUser.getId().toString());
                        response.put("fileId", fileToReturn.getId().toString());

                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(401).body("パスワードが間違っています");
                    }
                })
                .orElse(ResponseEntity.status(404).body("ユーザーが見つかりません"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("ユーザー名は既に存在します");
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}