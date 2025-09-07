package com.zeroichi.app.repository;

import com.zeroichi.app.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    // 必要に応じて、カスタムメソッドを追加します
}