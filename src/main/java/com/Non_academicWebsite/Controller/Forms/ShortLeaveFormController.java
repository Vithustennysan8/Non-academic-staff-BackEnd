package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.ShortLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.ShortLeaveForm;
import com.Non_academicWebsite.Service.Forms.ShortLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/short_leave_form")
public class ShortLeaveFormController {
    @Autowired
    private ShortLeaveFormService shortLeaveFormService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> submitForm(@ModelAttribute ShortLeaveFormDTO shortLeaveFormDTO,
                                        @RequestParam(value = "files", required = false)MultipartFile file,
                                        @RequestHeader("Authorization") String header) throws IOException {

        return ResponseEntity.ok(shortLeaveFormService.submitForm(shortLeaveFormDTO,file, header));
    }

    @GetMapping(value = "/get/{prefix}")
    public ResponseEntity<List<ShortLeaveForm>> getForms(@PathVariable("prefix") String prefix){
        return ResponseEntity.ok(shortLeaveFormService.getForms(prefix));
    }
}
