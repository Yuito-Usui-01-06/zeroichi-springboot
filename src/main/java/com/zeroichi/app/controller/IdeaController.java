package com.zeroichi.app.controller;

import com.zeroichi.app.domain.Idea;
import com.zeroichi.app.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ideas")
public class IdeaController {

    @Autowired
    private IdeaRepository ideaRepository;

    @PostMapping
    public Idea createIdea(@RequestBody Idea idea) {
        // 💡 サーバー側での確認用ログ
        System.out.println("Received Idea: " + idea.toString());
        return ideaRepository.save(idea);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<Idea>> getIdeasByFileId(@PathVariable Long fileId) {
        List<Idea> ideas = ideaRepository.findByFileId(fileId);
        return ResponseEntity.ok(ideas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Idea> updateIdea(@PathVariable Long id, @RequestBody Idea ideaDetails) {
        return ideaRepository.findById(id)
                .map(idea -> {
                    idea.setTitle(ideaDetails.getTitle());
                    idea.setDescription(ideaDetails.getDescription());
                    idea.setPosX(ideaDetails.getPosX());
                    idea.setPosY(ideaDetails.getPosY());
                    idea.setTags(ideaDetails.getTags()); //タグの更新処理
                    idea.setUpdatedAt(LocalDateTime.now());
                    Idea updatedIdea = ideaRepository.save(idea);
                    return ResponseEntity.ok(updatedIdea);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdea(@PathVariable Long id) {
        if (ideaRepository.existsById(id)) {
            ideaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Idea>> getIdeasByUserId(@PathVariable Long userId) {
        // IdeaRepositoryにfindByUserIdメソッドを実装する必要があります
        List<Idea> ideas = ideaRepository.findByUserId(userId);
        return ResponseEntity.ok(ideas);
    }
}