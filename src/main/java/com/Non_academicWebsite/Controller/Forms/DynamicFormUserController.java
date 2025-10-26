package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.Entity.Forms.DynamicFormFileDetail;
import com.Non_academicWebsite.Service.Forms.DynamicFormUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class DynamicFormUserController {

    @Autowired
    private DynamicFormUserService dynamicFormUserService;

    @GetMapping(value = "/auth/user/DynamicFormUser/getAll")
    public ResponseEntity<?> getAllFormApplied(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(dynamicFormUserService.getAllFormApplied(header));
    }

    @GetMapping(value = "/auth/user/DynamicFormUser/getAllById/{id}")
    public ResponseEntity<?> getAllFormAppliedBYId(@PathVariable("id") String id){
        return ResponseEntity.ok(dynamicFormUserService.getAllFormAppliedBYId(id));
    }

    @GetMapping("/auth/user/DynamicFormUser/getPdf/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable("id") Long id) {
        DynamicFormFileDetail fileData = dynamicFormUserService.generatePdf(id);

        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileData.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFileName() + "\"")
                .body(fileData.getFile());
    }

    @GetMapping(value = "admin/DynamicFormUser/getAll")
    public ResponseEntity<?> getAllFormRequests(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(dynamicFormUserService.getAllFormRequests(header));
    }
}
