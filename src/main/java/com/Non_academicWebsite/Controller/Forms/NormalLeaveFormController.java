package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.Forms.NormalLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Service.Forms.NormalLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/user/normalLeaveForm")
public class NormalLeaveFormController {
    @Autowired
    private NormalLeaveFormService normalLeaveFormService;


    @PostMapping(value = "/add")
    public ResponseEntity<NormalLeaveForm> addForm(@RequestHeader("Authorization") String header,
                                                   @RequestBody NormalLeaveFormDTO normalLeaveFormDTO)
            throws UnauthorizedAccessException, ResourceNotFoundException {
        return ResponseEntity.ok(normalLeaveFormService.add(header, normalLeaveFormDTO));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<NormalLeaveForm>> getForms(@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(normalLeaveFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<NormalLeaveForm>> getFormsOFUser(@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(normalLeaveFormService.getFormsOfUser(header));
    }

    @GetMapping(value = "/getPending")
    public ResponseEntity<List<NormalLeaveForm>> getFormsPending(@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(normalLeaveFormService.getFormsPending(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header)
            throws FormUnderProcessException, ResourceNotFoundException {
        return ResponseEntity.ok(normalLeaveFormService.deleteByUser(id, header));
    }
}
