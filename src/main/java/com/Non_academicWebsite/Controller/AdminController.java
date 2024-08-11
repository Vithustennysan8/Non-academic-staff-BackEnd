package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Service.AdminService;
import com.Non_academicWebsite.Service.Forms.FullLeaveFormService;
import com.Non_academicWebsite.Service.Forms.ShortLeaveFormService;
import com.Non_academicWebsite.Service.Forms.SubtituteFormService;
import com.Non_academicWebsite.Service.Forms.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {
    @Autowired
    private FullLeaveFormService fullLeaveFormService;
    @Autowired
    private ShortLeaveFormService shortLeaveFormService;
    @Autowired
    private SubtituteFormService subtituteFormService;
    @Autowired
    private TransferFormService transferFormService;
    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> getLeaveForms() {
        return ResponseEntity.ok("ADMIN::get");
    }

    @PostMapping(value = "/req/fullLeaveForm")
    public ResponseEntity<?> getReqFullLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(fullLeaveFormService.getFullLeaveForms(reqFormsDTO));
    }
    @PostMapping(value = "/req/shortLeaveForm")
    public ResponseEntity<?> getReqShortLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(shortLeaveFormService.getShortLeaveForms(reqFormsDTO));
    }
    @PostMapping(value = "/req/subtituteForm")
    public ResponseEntity<?> getReqSubtituteForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(subtituteFormService.getSubtituteForms(reqFormsDTO));
    }
    @PostMapping(value = "/req/transferForm")
    public ResponseEntity<?> getReqTransferForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(transferFormService.getTransferForms(reqFormsDTO));
    }

    @PostMapping(value = "/req/allForms")
    public ResponseEntity<?> getAllReqForms(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(adminService.getAllReqForms(reqFormsDTO));
    }
}
