package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.DTO.Forms.MedicalLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
import com.Non_academicWebsite.Service.Forms.MedicalLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/auth/medicalLeaveForm")
public class MedicalLeaveFormController {
    @Autowired
    private MedicalLeaveFormService medicalLeaveFormService;

    @PostMapping(value = "/add")
    public ResponseEntity<MedicalLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                    @ModelAttribute MedicalLeaveFormDTO medicalLeaveFormDTO,
                                                    @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(medicalLeaveFormService.add(header, medicalLeaveFormDTO, file));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<MedicalLeaveForm>> getForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(medicalLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<MedicalLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(medicalLeaveFormService.getFormsOfUser(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header) throws FormUnderProcessException {
        return ResponseEntity.ok(medicalLeaveFormService.deleteByUser(id, header));
    }
}
