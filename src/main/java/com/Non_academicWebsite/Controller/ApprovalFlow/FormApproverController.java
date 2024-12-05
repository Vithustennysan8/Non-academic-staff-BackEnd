package com.Non_academicWebsite.Controller.ApprovalFlow;

import com.Non_academicWebsite.Service.ApprovalFlow.FormApproverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin/formApprover")
public class FormApproverController {

    @Autowired
    private FormApproverService formApproverService;

    @GetMapping(value = "/get/{formId}")
    public ResponseEntity<?> getApprover(@RequestHeader("Authorization") String header,
                                         @PathVariable("formId") Long formId){
        return ResponseEntity.ok(formApproverService.getApprover(formId, header));
    }
}
