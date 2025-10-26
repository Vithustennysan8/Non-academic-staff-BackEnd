package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.ForumDTO;
import com.Non_academicWebsite.Entity.Forum;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ForumService {

    @Autowired
    private ForumRepo forumRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ExtractUserService extractUserService;

    public Object addForum(String header, ForumDTO forumDTO) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        var forum = Forum.builder()
                .user(user)
                .userName(user.getFirst_name().concat(" " + user.getLast_name()))
                .subject(forumDTO.getSubject())
                .body(forumDTO.getBody())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return forumRepo.save(forum);
    }

    public List<Forum> getForums(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        String id = user.getId();
        String prefix = id.substring(0, id.length() - 7);

        List<Forum> forums = forumRepo.findByUserIdStartingWith(prefix);
        if (forums.isEmpty()) {
            return Collections.emptyList();
        }
        return forums;
    }

    public List<Forum> deleteForum(Long id, String header) throws Exception {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if (forumRepo.existsById(id)) {
            Forum forum = forumRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Forum not found "+ id +" with this id."));
            if (Objects.equals(forum.getUser().getId(), user.getId()) || user.getRole() == Role.ADMIN) {
                forumRepo.deleteById(id);
            }
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        List<Forum> forums = forumRepo.findByUserIdStartingWith(prefix);
        if (forums.isEmpty()) {
            return Collections.emptyList();
        }
        return forums;
    }

    public List<Forum> updateForum(Long id, ForumDTO forumDTO, String header) throws Exception{
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);
        if (forumRepo.existsById(id)) {
            Forum forum = forumRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Forum not found "+ id +" with this id."));
            if (Objects.equals(forum.getUser().getId(), user.getId())) {
                forum.setUserName(user.getFirst_name().concat(" " + user.getLast_name()));
                forum.setSubject(forumDTO.getSubject());
                forum.setBody(forumDTO.getBody());
                forum.setUpdatedAt(new Date());

                forumRepo.save(forum);
            }
        }

        List<Forum> forums = forumRepo.findByUserIdStartingWith(prefix);
        if (forums.isEmpty()) {
            return Collections.emptyList();
        }
        return forums;

    }
}
