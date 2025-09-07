package com.zeroichi.app.repository;

import com.zeroichi.app.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    // 追加のデータベース操作が必要な場合に、ここにメソッドを定義します
    List<Idea> findByFileId(Long fileId);

    List<Idea> findByUserId(Long userId);
}