package com.zeroichi.app.controller;

import com.zeroichi.app.domain.File;
import com.zeroichi.app.domain.User;
import com.zeroichi.app.repository.FileRepository;
import com.zeroichi.app.repository.UserRepository;
import com.zeroichi.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        System.out.println("=== LOGIN DEBUG START ===");
        System.out.println("Input username: " + user.getUsername());
        System.out.println("Input password: " + user.getPassword());

        return userRepository.findByUsername(user.getUsername())
                .map(foundUser -> {
                    if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                        System.out.println("Password match: SUCCESS");

                        // ログイン成功後、ユーザーに紐づく既存のファイルを検索
                        List<File> existingFiles = fileRepository.findByUserId(foundUser.getId());
                        File fileToReturn;

                        if (existingFiles.isEmpty()) {
                            // 既存ファイルがない場合のみ、新しいファイルを作成
                            File newFile = new File();
                            newFile.setName("新規ホワイトボード");
                            newFile.setUserId(foundUser.getId());
                            newFile.setCreatedAt(LocalDateTime.now());
                            newFile.setUpdatedAt(LocalDateTime.now());
                            fileToReturn = fileRepository.save(newFile);
                        } else {
                            // 既存ファイルがある場合、最初のファイルを使用
                            fileToReturn = existingFiles.get(0);
                        }

                        // ユーザーIDと返却するファイルIDをレスポンスとして返す
                        Map<String, String> response = new HashMap<>();
                        response.put("userId", foundUser.getId().toString());
                        response.put("fileId", fileToReturn.getId().toString());
                        response.put("role", foundUser.getRole());

                        System.out.println("Response role: " + foundUser.getRole());
                        System.out.println("=== LOGIN DEBUG SUCCESS ===");

                        return ResponseEntity.ok(response);
                    } else {
                        System.out.println("Password match: FAILED");
                        System.out.println("passwordEncoder.matches() returned false");
                        System.out.println("=== LOGIN DEBUG FAILED ===");
                        return ResponseEntity.status(401).body("パスワードが間違っています");
                    }
                })
                .orElse(ResponseEntity.status(404).body("ユーザーが見つかりません"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ユーザー名は既に存在します");
        }
    }

    // 全ユーザーを取得するAPIエンドポイント
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        // ユーザーのリストを全て取得
        List<User> users = (List<User>) userRepository.findAll();
        // パスワード情報をレスポンスから削除
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // ユーザーを削除するAPIエンドポイント
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}