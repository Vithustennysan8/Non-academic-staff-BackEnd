package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.FullLeaveFormDTO;
import com.Non_academicWebsite.Repository.FullLeaveFormRepo;
import com.Non_academicWebsite.Service.FullLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/full_leave_form")
public class FullLeaveFormController {
    @Autowired
    private FullLeaveFormService fullLeaveFormService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> submitForm(@ModelAttribute FullLeaveFormDTO fullLeaveFormDTO,
                                        @RequestParam(value = "files", required = false) MultipartFile file ) throws IOException {

        return ResponseEntity.ok(fullLeaveFormService.submitForm(fullLeaveFormDTO,file));
    }
}
