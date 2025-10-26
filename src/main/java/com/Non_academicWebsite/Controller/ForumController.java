package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.ForumDTO;
import com.Non_academicWebsite.Entity.Forum;
import com.Non_academicWebsite.Service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/auth/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addForum(@RequestHeader("Authorization") String header,
                                      @RequestBody ForumDTO forumDTO) throws ResourceNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.addForum(header, forumDTO));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Forum>> getForums(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(forumService.getForums(header));
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<List<Forum>> deleteForum(@PathVariable("id") Long id,
                                                   @RequestHeader("Authorization") String header) throws Exception {
        return ResponseEntity.ok(forumService.deleteForum(id, header));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<List<Forum>> updateForum(@PathVariable("id") Long id, @RequestBody ForumDTO forumDTO,
                                                   @RequestHeader("Authorization") String header) throws Exception {
        return ResponseEntity.ok(forumService.updateForum(id, forumDTO, header));
    }
}
