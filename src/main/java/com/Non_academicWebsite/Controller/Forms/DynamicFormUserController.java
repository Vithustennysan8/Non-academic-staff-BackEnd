package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.Service.Forms.DynamicFormUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/")
public class DynamicFormUserController {

    @Autowired
    private DynamicFormUserService dynamicFormUserService;

    @GetMapping(value = "/auth/user/DynamicFormUser/getAll")
    public ResponseEntity<?> getAllFormApplied(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(dynamicFormUserService.getAllFormApplied(header));
    }

    @GetMapping(value = "admin/DynamicFormUser/getAll")
    public ResponseEntity<?> getAllFormRequests(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(dynamicFormUserService.getAllFormRequests(header));
    }
}
