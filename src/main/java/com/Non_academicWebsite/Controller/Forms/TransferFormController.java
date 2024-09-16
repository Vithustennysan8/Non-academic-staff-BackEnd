package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.TransferFromDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Service.Forms.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/transfer_form")
public class TransferFormController {
    @Autowired
    private TransferFormService transferFormService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> submitForm(@ModelAttribute TransferFromDTO transferFromDTO,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestHeader("Authorization") String header) throws IOException {

        return ResponseEntity.ok(transferFormService.submitForm(transferFromDTO,file,header));
    }

    @GetMapping(value = "/get/{prefix}")
    public ResponseEntity<List<TransferForm>> getForms(@PathVariable("prefix") String prefix){
        return ResponseEntity.ok(transferFormService.getForms(prefix));
    }
}
