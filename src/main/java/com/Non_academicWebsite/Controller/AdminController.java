package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
import com.Non_academicWebsite.Entity.Forms.PaternalLeaveForm;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Service.AdminService;
import com.Non_academicWebsite.Service.AuthenticationService;
import com.Non_academicWebsite.Service.Forms.*;
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
    private PaternalLeaveFormService paternalLeaveFormService;
    @Autowired
    private MaternityLeaveFormService maternityLeaveFormService;
    @Autowired
    private MedicalLeaveFormService medicalLeaveFormService;
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
                                            @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(adminService.deleteUserById(id, header));
    }

    // used to retrieve the all registered user for verification
    @GetMapping(value = "/verifyRequests")
    public ResponseEntity<List<RegisterConfirmationToken>> getVerifyRequests(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(confirmationTokenService.getVerifyRequests(header));
    }

    // used to verify the newly registered user
    @PutMapping(value = "/verify/{token}")
    public ResponseEntity<List<RegisterConfirmationToken>> confirmUser(@PathVariable("token") String token,
                                              @RequestHeader("Authorization") String header) {
        return ResponseEntity.ok(authenticationService.confirmUser(token, header));
    }

    // used to retrieve the all NormalLeaveForm
    @PostMapping(value = "/req/normalLeaveForm")
    public ResponseEntity<?> getReqNormalLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, header));
    }

    // used to retrieve the all AccidentLeaveForm
    @PostMapping(value = "/req/accidentLeaveForm")
    public ResponseEntity<?> getReqAccidentLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header));
    }

    @PostMapping(value = "/req/paternalLeaveForm")
    public ResponseEntity<List<PaternalLeaveForm>> getReqPaternalLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                                           @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(paternalLeaveFormService.getPaternalLeaveForms(reqFormsDTO, header));
    }

    @PostMapping(value = "/req/maternityLeaveForm")
    public ResponseEntity<List<MaternityLeaveForm>> getReqMaternityLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                                             @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(maternityLeaveFormService.getMaternityLeaveForms(reqFormsDTO, header));
    }

    @PostMapping(value = "/req/medicalLeaveForm")
    public ResponseEntity<List<MedicalLeaveForm>> getReqMedicalLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO,
                                                                         @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(medicalLeaveFormService.getMedicalLeaveForms(reqFormsDTO, header));
    }

    // used to retrieve the all TransferForm
    @PostMapping(value = "/req/transferForm")
    public ResponseEntity<?> getReqTransferForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(transferFormService.getTransferForms(reqFormsDTO));
    }

    // used to retrieve the all LeaveRequests
    @GetMapping("/leaveForms/notify")
    public ResponseEntity<?> notifyAllLeaveFormRequests(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(adminService.getAllFormRequests(header));
    }

    // used to accept the leave form
    @PutMapping(value = "/accept/{id}")
    public ResponseEntity<?> acceptLeaveForm(@PathVariable("id") Long formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.acceptForm(formId, approvalDTO));
            case "Accident Leave Form" -> ResponseEntity.ok(accidentLeaveFormService.acceptForm(formId, approvalDTO));
            case "Paternal Leave Form" -> ResponseEntity.ok(paternalLeaveFormService.acceptForm(formId, approvalDTO));
            case "Maternity Leave Form" -> ResponseEntity.ok(maternityLeaveFormService.acceptForm(formId, approvalDTO));
            case "Medical Leave Form" -> ResponseEntity.ok(medicalLeaveFormService.acceptForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

    // used to reject the leave form
    @PutMapping(value = "/reject/{id}")
    public ResponseEntity<?> rejectLeaveForm(@PathVariable("id") Long formId,
                                                   @RequestBody ApprovalDTO approvalDTO){
        return switch (approvalDTO.getFormType()) {
            case "Normal Leave Form" -> ResponseEntity.ok(normalLeaveFormService.rejectForm(formId, approvalDTO));
            case "Accident Leave Form" -> ResponseEntity.ok(accidentLeaveFormService.rejectForm(formId, approvalDTO));
            case "Paternal Leave Form" -> ResponseEntity.ok(paternalLeaveFormService.rejectForm(formId, approvalDTO));
            case "Maternity Leave Form" -> ResponseEntity.ok(maternityLeaveFormService.rejectForm(formId, approvalDTO));
            case "Medical Leave Form" -> ResponseEntity.ok(medicalLeaveFormService.rejectForm(formId, approvalDTO));
            default -> ResponseEntity.ok("Failed");
        };
    }

}
