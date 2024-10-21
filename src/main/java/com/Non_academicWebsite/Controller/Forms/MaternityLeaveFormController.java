package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.MaternityLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import com.Non_academicWebsite.Service.Forms.MaternityLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/auth/maternityLeaveForm")
public class MaternityLeaveFormController {
    @Autowired
    private MaternityLeaveFormService maternityLeaveFormService;

    @PostMapping(value = "/add")
    public ResponseEntity<MaternityLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                      @ModelAttribute MaternityLeaveFormDTO maternityLeaveFormDTO,
                                                      @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(maternityLeaveFormService.add(header, maternityLeaveFormDTO, file));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<MaternityLeaveForm>> getForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(maternityLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<MaternityLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(maternityLeaveFormService.getFormsOfUser(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(maternityLeaveFormService.deleteByUser(id, header));
    }
}
