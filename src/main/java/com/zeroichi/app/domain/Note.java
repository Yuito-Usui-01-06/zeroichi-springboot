package com.zeroichi.app.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*; // 💡 ここを jakarta に変更
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Double posX;
    private Double posY;
    private Long fileId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}