package com.Non_academicWebsite.Controller.ApprovalFlow;

import com.Non_academicWebsite.DTO.ApprovalFlow.ApprovalFlowDTO;
import com.Non_academicWebsite.Service.ApprovalFlow.ApprovalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api")
@CrossOrigin
public class ApprovalFlowController {

    @Autowired
    private ApprovalFlowService approvalFlowService;

    // Add new approval flow for the newly added forms flow
    @PostMapping(value = "/admin/approvalFlow/add")
    public ResponseEntity<Object> addApprovalFlow(@RequestBody ApprovalFlowDTO approvalFlowDTO,
                                                              @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(approvalFlowService.addNewApprovalFlow(approvalFlowDTO, header));
    }

    @GetMapping(value = "/auth/user/approvalFlow/get/{form}")
    public ResponseEntity<?> getApprovalFlowByForm(@PathVariable("form") String form,
                                                   @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(approvalFlowService.getApprovalFlowByForm(form, header));
    }

    @PutMapping(value = "/admin/approvalFlow/update")
    public ResponseEntity<Object> updateApprovalFlow(@RequestBody ApprovalFlowDTO approvalFlowDTO,
                                                           @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(approvalFlowService.updateApprovalFlow(approvalFlowDTO, header));
    }

    @GetMapping(value = "/admin/approvalFlow/getAll")
    public ResponseEntity<?> getAllApprovalFlows(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(approvalFlowService.getAllApprovalFlowsByDepartment(header));
    }

    @DeleteMapping(value = "/admin/approvalFlow/delete")
    public ResponseEntity<Object> deleteApprovalFlow(@RequestHeader("Authorization") String header,
                                                                 @RequestBody ApprovalFlowDTO approvalFlowDTO){
        return ResponseEntity.ok(approvalFlowService.deleteApprovalFlow(header, approvalFlowDTO));
    }
}
