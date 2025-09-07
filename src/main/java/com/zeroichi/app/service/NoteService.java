package com.zeroichi.app.service;

import com.zeroichi.app.domain.Note;
import com.zeroichi.app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> findNotesByFileId(Long fileId) {
        return noteRepository.findByFileId(fileId);
    }
}