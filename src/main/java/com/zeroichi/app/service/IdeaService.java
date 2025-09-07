package com.zeroichi.app.service;

import com.zeroichi.app.domain.Idea;
import com.zeroichi.app.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;

    public List<Idea> findIdeasByFileId(Long fileId) {
        return ideaRepository.findByFileId(fileId);
    }
}
