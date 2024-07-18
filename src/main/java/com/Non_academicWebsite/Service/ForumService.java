package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ForumDTO;
import com.Non_academicWebsite.Entity.Forum;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Service
public class ForumService {

    @Autowired
    private ForumRepo forumRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    public Object addForum(String header, ForumDTO forumDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow();

        var forum = Forum.builder()
                .userId(user.getId())
                .subject(forumDTO.getSubject())
                .body(forumDTO.getBody())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return forumRepo.save(forum);
    }

    public List<Forum> getForums() {
        return forumRepo.findAll();
    }
}
