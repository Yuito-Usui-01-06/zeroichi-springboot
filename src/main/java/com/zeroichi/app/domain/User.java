package com.zeroichi.app.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Long roleId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // roleフィールドを削除（DDLで削除済みのため）

    public User() {
        this.roleId = 2L; // デフォルトでUSERロール（ID=2）
    }

    // roleIdから役割を判定するメソッド
    public String getRole() {
        if (roleId != null && roleId == 1L) {
            return "ADMIN";
        }
        return "USER";
    }
}