package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Service.Forms.AccidentLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/auth/accidentLeaveForm")
public class AccidentLeaveFormController {
    @Autowired
    private AccidentLeaveFormService accidentLeaveFormService;


    @PostMapping(value = "/add")
    public ResponseEntity<AccidentLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                     @ModelAttribute AccidentLeaveFormDTO accidentLeaveFormDTO,
                                                     @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(accidentLeaveFormService.add(header, accidentLeaveFormDTO, file));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<AccidentLeaveForm>> getForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<AccidentLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getFormsOfUser(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header) throws FormUnderProcessException {
        return ResponseEntity.ok(accidentLeaveFormService.deleteByUser(id, header));
    }
}
