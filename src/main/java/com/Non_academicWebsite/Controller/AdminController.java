package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Service.AdminService;
import com.Non_academicWebsite.Service.AuthenticationService;
import com.Non_academicWebsite.Service.Forms.AccidentLeaveFormService;
import com.Non_academicWebsite.Service.Forms.NormalLeaveFormService;
import com.Non_academicWebsite.Service.Forms.TransferFormService;
import com.Non_academicWebsite.Service.RegisterConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {
    @Autowired
    private NormalLeaveFormService normalLeaveFormService;
    @Autowired
    private AccidentLeaveFormService accidentLeaveFormService;
    @Autowired
    private TransferFormService transferFormService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RegisterConfirmationTokenService confirmationTokenService;
    @Autowired
    private AuthenticationService authenticationService;


    @GetMapping(value = "/get")
    public ResponseEntity<?> getLeaveForms() {
        return ResponseEntity.ok("ADMIN::get");
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id,
                                            @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(adminService.deleteUserById(id, header));
    }
    @GetMapping(value = "/verifyRequests")
    public ResponseEntity<List<RegisterConfirmationToken>> getVerifyRequests(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(confirmationTokenService.getVerifyRequests(header));
    }

    @PutMapping(value = "/verify/{token}")
    public ResponseEntity<List<RegisterConfirmationToken>> confirmUser(@PathVariable("token") String token,
                                              @RequestHeader("Authorization") String header) {
        return ResponseEntity.ok(authenticationService.confirmUser(token, header));
    }

    @PostMapping(value = "/req/normalLeaveForm")
    public ResponseEntity<?> getReqNormalLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, header));
    }
    @PostMapping(value = "/req/accidentLeaveForm")
    public ResponseEntity<?> getReqAccidentLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header));
    }

    @PostMapping(value = "/req/transferForm")
    public ResponseEntity<?> getReqTransferForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(transferFormService.getTransferForms(reqFormsDTO));
    }

    @GetMapping("/leaveForms/notify")
    public ResponseEntity<?> notifyLeaveFormRequests(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(adminService.getAllFormRequests(header));
    }

    @PutMapping(value = "/accept/{id}")
    public ResponseEntity<?> acceptNormalLeaveForm(@PathVariable("id") Integer formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.acceptForm(formId, approvalDTO));
            case "Accident Leave Form" -> ResponseEntity.ok(accidentLeaveFormService.acceptForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

    @PutMapping(value = "/reject/{id}")
    public ResponseEntity<?> rejectNormalLeaveForm(@PathVariable("id") Integer formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.rejectForm(formId, approvalDTO));
            case "Accident Leave Form" -> ResponseEntity.ok(accidentLeaveFormService.rejectForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

}
