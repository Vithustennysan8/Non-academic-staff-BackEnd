package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.SubtituteFormDTO;
import com.Non_academicWebsite.Service.Forms.SubtituteFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/auth/subtitute_form")
public class SubtituteFormController {
    @Autowired
    private SubtituteFormService subtituteFormService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> submitForm(@RequestBody SubtituteFormDTO subtituteFormDTO){

        return ResponseEntity.ok(subtituteFormService.submitForm(subtituteFormDTO));
    }
}
