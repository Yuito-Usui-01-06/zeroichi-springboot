package com.zeroichi.app.service;

import com.zeroichi.app.domain.File;
import com.zeroichi.app.domain.User;
import com.zeroichi.app.repository.FileRepository;
import com.zeroichi.app.repository.IdeaRepository;
import com.zeroichi.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleId(2L); // USERロール（ID=2）に設定
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        List<File> files = fileRepository.findByUserId(userId);
        for (File file : files) {
            ideaRepository.deleteByFileId(file.getId()); // Idea を先に削除
        }
        fileRepository.deleteByUserId(userId); // File を削除
        userRepository.deleteById(userId);      // 最後に User を削除
    }
}