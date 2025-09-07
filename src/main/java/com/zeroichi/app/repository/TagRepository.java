package com.zeroichi.app.repository;

import com.zeroichi.app.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // 必要に応じて、カスタムメソッドを追加します
}