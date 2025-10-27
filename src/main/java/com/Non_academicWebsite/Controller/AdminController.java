package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Service.AdminService;
import com.Non_academicWebsite.Service.AuthenticationService;
import com.Non_academicWebsite.Service.Forms.*;
import com.Non_academicWebsite.Service.RegisterConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
    @Autowired
    private NormalLeaveFormService normalLeaveFormService;
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

    // Admin can delete a user by ID
    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id,
                                            @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(adminService.deleteUserById(id, header));
    }

    // used to retrieve the all registered user for verification
    @GetMapping(value = "/verifyRegisterRequests")
    public ResponseEntity<List<RegisterConfirmationToken>> getVerifyRequests(@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(confirmationTokenService.getVerifyRequests(header));
    }

    // used to retrieve the admin register requests
    @GetMapping(value = "/verifyAdminRegisterRequests")
    public ResponseEntity<List<RegisterConfirmationToken>> getVerifyAdminRegisterRequests
                                                            (@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(confirmationTokenService.getVerifyAdminRegisterRequests(header));
    }


    // used to verify the newly registered user
    @PutMapping(value = "/verify/{token}")
    public ResponseEntity<List<RegisterConfirmationToken>> confirmUser(@PathVariable("token") String token,
                                              @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.confirmUser(token, header));
    }

    // used to retrieve the all NormalLeaveForm
    @PostMapping(value = "/req/normalLeaveForm")
    public ResponseEntity<?> getReqNormalLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.status(HttpStatus.CREATED).body(normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, header));
    }

    // used to retrieve the all TransferForm
    @PostMapping(value = "/req/transferForm")
    public ResponseEntity<?> getReqTransferForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                 @RequestHeader("Authorization") String header){
        return ResponseEntity.status(HttpStatus.CREATED).body(transferFormService.getTransferForms(reqFormsDTO, header));
    }

    // used to retrieve the all LeaveRequests
    @GetMapping("/leaveForms/notify")
    public ResponseEntity<?> notifyAllLeaveFormRequests(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(adminService.getAllLeaveFormRequests(header));
    }

    @GetMapping("/leaveForms/notification")
    public ResponseEntity<?> pendingNotificationLeaveFormRequestsByApprover(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(adminService.getPendingLeaveFormRequestsByApprover(header));
    }

    @GetMapping("/transferForms/notification")
    public ResponseEntity<List<TransferForm>> notifyAllTransferFormRequests(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(adminService.getAllTransferFormRequests(header));
    }

    @GetMapping(value = "leaveForms/getAllForms")
    public ResponseEntity<?> getAllLeaveForm(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(adminService.getAllLeaveForms(header));
    }

    // used to accept the leave form
    @PutMapping(value = "/accept/{id}")
    public ResponseEntity<?> acceptLeaveForm(@PathVariable("id") Long formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.acceptForm(formId, approvalDTO));
            case "Transfer Form" -> ResponseEntity.ok(transferFormService.acceptForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

    // used to reject the leave form
    @PutMapping(value = "/reject/{id}")
    public ResponseEntity<?> rejectLeaveForm(@PathVariable("id") Long formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.rejectForm(formId, approvalDTO));
            case "Transfer Form" -> ResponseEntity.ok(transferFormService.rejectForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

}
