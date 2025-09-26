package com.zeroichi.app.repository;

import com.zeroichi.app.domain.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}