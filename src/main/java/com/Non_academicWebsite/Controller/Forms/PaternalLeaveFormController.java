package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.DTO.Forms.PaternalLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.Forms.PaternalLeaveForm;
import com.Non_academicWebsite.Service.Forms.PaternalLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/auth/paternalLeaveForm")
public class PaternalLeaveFormController {
    @Autowired
    private PaternalLeaveFormService paternalLeaveFormService;

    @PostMapping(value = "/add")
    public ResponseEntity<PaternalLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                     @ModelAttribute PaternalLeaveFormDTO paternalLeaveFormDTO,
                                                     @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(paternalLeaveFormService.add(header, paternalLeaveFormDTO, file));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<PaternalLeaveForm>> getForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(paternalLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<PaternalLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(paternalLeaveFormService.getFormsOfUser(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(paternalLeaveFormService.deleteByUser(id, header));
    }

}
