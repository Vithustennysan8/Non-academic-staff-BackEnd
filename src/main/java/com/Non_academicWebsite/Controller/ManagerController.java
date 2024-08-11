package com.Non_academicWebsite.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/management")
public class ManagerController {

    @GetMapping(value = "/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok("Manager::get");
    }

}
