package com.Non_academicWebsite.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {

    @GetMapping(value = "/get")
    public ResponseEntity<?> getLeaveForms() {
        return ResponseEntity.ok("");
    }
}
