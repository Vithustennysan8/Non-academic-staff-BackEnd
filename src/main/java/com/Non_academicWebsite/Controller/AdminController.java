package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Sample;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.SampleRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.AdminService;
import com.Non_academicWebsite.Service.Forms.FullLeaveFormService;
import com.Non_academicWebsite.Service.Forms.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {
    @Autowired
    private FullLeaveFormService fullLeaveFormService;
    @Autowired
    private TransferFormService transferFormService;
    @Autowired
    private SampleRepo sampleRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> getLeaveForms() {
        return ResponseEntity.ok("ADMIN::get");
    }

    @PostMapping(value = "/req/fullLeaveForm")
    public ResponseEntity<?> getReqFullLeaveForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(fullLeaveFormService.getFullLeaveForms(reqFormsDTO));
    }

    @PostMapping(value = "/req/transferForm")
    public ResponseEntity<?> getReqTransferForm(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(transferFormService.getTransferForms(reqFormsDTO));
    }

    @PostMapping(value = "/req/allForms")
    public ResponseEntity<?> getAllReqForms(@RequestBody ReqFormsDTO reqFormsDTO){
        return ResponseEntity.ok(adminService.getAllReqForms(reqFormsDTO));
    }


    @PostMapping(value = "/req/sample")
    public ResponseEntity<List<Sample>> getReqSample(@RequestBody ReqFormsDTO reqFormsDTO, @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();

        return switch (user.getJob_type()) {
            case "Technical Officer" -> {
                if (reqFormsDTO.getDepartment().isEmpty()) {
                    yield ResponseEntity.ok(sampleRepo.findByFaculty(reqFormsDTO.getFaculty()));
                }
                yield ResponseEntity.ok(sampleRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment()));
            }
            case "Management Assistant" -> {
                if (reqFormsDTO.getDepartment().isEmpty()) {
                    yield ResponseEntity.ok(sampleRepo.findByFacultyAndApprover1(reqFormsDTO.getFaculty(), "Accepted"));
                }
                yield ResponseEntity.ok(sampleRepo.findByFacultyAndDepartmentAndApprover1(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment(), "Accepted"));
            }
            default -> ResponseEntity.ok(Collections.emptyList());
        };
    }

    @PutMapping(value = "/accept/{id}")
    public ResponseEntity<?> acceptForm(@PathVariable("id") Integer formId, @RequestBody User admin){
        Sample sample = sampleRepo.findById(formId).orElse(null);
        if(sample != null){
            String job = admin.getJob_type();
            switch (job){
                case "Technical Officer":
                    sample.setApprover1("Accepted");
                    break;

                case "Management Assistant":
                    sample.setApprover2("Accepted");
                    break;
            }

            return ResponseEntity.ok(sampleRepo.save(sample));
        }
        return ResponseEntity.ok("Failed");
    }
    @PutMapping(value = "/reject/{id}")
    public ResponseEntity<?> rejectForm(@PathVariable("id") Integer formId, @RequestBody User admin){
        Sample sample = sampleRepo.findById(formId).orElse(null);
        if(sample != null){
            String job = admin.getJob_type();
            switch (job){
                case "Technical Officer":
                    sample.setApprover1("Rejected");
                    break;

                case "Management Assistant":
                    sample.setApprover2("Rejected");
                    break;
            }
            return ResponseEntity.ok(sampleRepo.save(sample));
        }
        return ResponseEntity.ok("Failed");
    }

}
