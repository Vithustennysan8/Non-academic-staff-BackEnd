package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Service.Forms.AccidentLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/accidentLeaveForm")
public class AccidentLeaveFormController {
    @Autowired
    private AccidentLeaveFormService accidentLeaveFormService;


    @PostMapping(value = "/add")
    public ResponseEntity<AccidentLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                   @RequestBody AccidentLeaveFormDTO accidentLeaveFormDTO){
        return ResponseEntity.ok(accidentLeaveFormService.add(header, accidentLeaveFormDTO));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<AccidentLeaveForm>> getForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<AccidentLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getFormsOfUser(header));
    }
}
