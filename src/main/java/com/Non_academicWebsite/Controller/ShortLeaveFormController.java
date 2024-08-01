package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.ShortLeaveFormDTO;
import com.Non_academicWebsite.Service.ShortLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/short_leave_form")
public class ShortLeaveFormController {
    @Autowired
    private ShortLeaveFormService shortLeaveFormService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> submitForm(@ModelAttribute ShortLeaveFormDTO shortLeaveFormDTO,
                                        @RequestParam(value = "files", required = false)MultipartFile file ) throws IOException {

        return ResponseEntity.ok(shortLeaveFormService.submitForm(shortLeaveFormDTO,file));
    }
}
