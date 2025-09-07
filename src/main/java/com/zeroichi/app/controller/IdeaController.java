package com.zeroichi.app.controller;

import com.zeroichi.app.domain.Idea;
import com.zeroichi.app.domain.Note;
import com.zeroichi.app.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zeroichi.app.service.IdeaService;
import com.zeroichi.app.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ideas")
public class IdeaController {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private NoteService noteService;

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

    @PostMapping("/link-related/{fileId}")
    public ResponseEntity<List<Idea>> linkRelatedIdeas(@PathVariable Long fileId) {
        List<Idea> ideas = ideaService.findIdeasByFileId(fileId);

        // 💡 関連性計算ロジック
        Map<Long, Set<String>> keywordMap = new HashMap<>();
        for (Idea idea : ideas) {
            Set<String> keywords = new HashSet<>();
            // タイトルを単語に分割
            Arrays.stream(idea.getTitle().split("\\s+")).forEach(keywords::add);
            // 説明文を単語に分割
            if (idea.getDescription() != null) {
                Arrays.stream(idea.getDescription().split("\\s+")).forEach(keywords::add);
            }
            keywordMap.put(idea.getId(), keywords);
        }

        // 💡 類似度に基づいてノードを紐付け
        List<Idea> updatedIdeas = new ArrayList<>();
        // 既存の関連性を一旦クリア
        for (Idea idea : ideas) {
            idea.setRelatedIdeaIds(new ArrayList<>());
        }

        for (int i = 0; i < ideas.size(); i++) {
            Idea idea1 = ideas.get(i);

            for (int j = 0; j < ideas.size(); j++) {
                if (i == j) continue;
                Idea idea2 = ideas.get(j);

                Set<String> keywords1 = keywordMap.get(idea1.getId());
                Set<String> keywords2 = keywordMap.get(idea2.getId());

                // 共通部分を計算
                Set<String> intersection = new HashSet<>(keywords1);
                intersection.retainAll(keywords2);

                // 共通キーワードの数が1つ以上なら双方向に紐付け
                if (intersection.size() >= 1) {
                    // idea1にidea2のIDを追加
                    if (!idea1.getRelatedIdeaIds().contains(idea2.getId())) {
                        idea1.getRelatedIdeaIds().add(idea2.getId());
                    }
                    // idea2にidea1のIDを追加（双方向の関連性）
                    if (!idea2.getRelatedIdeaIds().contains(idea1.getId())) {
                        idea2.getRelatedIdeaIds().add(idea1.getId());
                    }
                }
            }
        }

        // 💡 変更をデータベースに保存
        updatedIdeas = ideaRepository.saveAll(ideas);

        return ResponseEntity.ok(updatedIdeas);
    }
}