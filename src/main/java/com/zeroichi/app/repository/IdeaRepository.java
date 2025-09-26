package com.zeroichi.app.repository;

import com.zeroichi.app.domain.Idea;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    // 追加のデータベース操作が必要な場合に、ここにメソッドを定義します
    List<Idea> findByFileId(Long fileId);

    List<Idea> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Idea i WHERE i.fileId = :fileId")
    void deleteByFileId(@Param("fileId") Long fileId);
}