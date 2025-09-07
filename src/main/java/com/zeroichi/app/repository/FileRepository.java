package com.zeroichi.app.repository;

import com.zeroichi.app.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    // 必要に応じて、カスタムメソッドを追加します
    List<File> findByUserId(Long userId);
}