package com.zeroichi.app.controller;

import com.zeroichi.app.domain.Note;
import com.zeroichi.app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime; // 💡 LocalDateTimeに変更
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/file/{fileId}")
    public List<Note> getNotesByFileId(@PathVariable Long fileId) {
        return noteRepository.findByFileId(fileId);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        note.setCreatedAt(LocalDateTime.now()); // 💡 LocalDateTime.now()に変更
        note.setUpdatedAt(LocalDateTime.now()); // 💡 LocalDateTime.now()に変更
        return noteRepository.save(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        Optional<Note> existingNote = noteRepository.findById(id);
        if (existingNote.isPresent()) {
            Note updatedNote = existingNote.get();
            updatedNote.setContent(note.getContent());
            updatedNote.setPosX(note.getPosX());
            updatedNote.setPosY(note.getPosY());
            updatedNote.setUpdatedAt(LocalDateTime.now()); // 💡 LocalDateTime.now()に変更
            return noteRepository.save(updatedNote);
        } else {
            return null; // or throw an exception
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
    }
}