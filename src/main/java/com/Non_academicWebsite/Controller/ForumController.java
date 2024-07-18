package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.ForumDTO;
import com.Non_academicWebsite.Entity.Forum;
import com.Non_academicWebsite.Service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/auth/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addForum(@RequestHeader("Authorization") String header,
                                           @RequestBody ForumDTO forumDTO){

        return ResponseEntity.ok(forumService.addForum(header, forumDTO));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Forum>> getForums(){
        return ResponseEntity.ok(forumService.getForums());
    }
}
