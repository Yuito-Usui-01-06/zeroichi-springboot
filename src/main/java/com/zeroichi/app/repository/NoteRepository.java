package com.zeroichi.app.repository;

import com.zeroichi.app.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByFileId(Long fileId);
}