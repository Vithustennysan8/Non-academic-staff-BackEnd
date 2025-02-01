package com.Non_academicWebsite.Service.ApprovalFlow;

import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ApprovalFlow.FormApproverRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import com.Non_academicWebsite.Service.Forms.DynamicFormUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class FormApproverService {

    @Autowired
    private FormApproverRepo formApproverRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private DynamicFormUserService dynamicFormUserService;

    public Object getApprover(Long formId, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<FormApprover> formApprovers = formApproverRepo.findByFormId(formId).stream()
                .sorted(Comparator.comparingInt(FormApprover::getApproverOrder)).toList();

        return null;
    }

    public Object getFormsApprover(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<FormApprover> formApprovers = formApproverRepo.findAll().stream()
                .sorted(Comparator.comparingInt(FormApprover::getApproverOrder)).toList();

        for (FormApprover formApp: formApprovers){
            
        }
        return null;
    }

    public Object acceptForm(Long approverId, ApprovalDTO approvalDTO, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        FormApprover formApp = formApproverRepo.findById(approverId).orElse(null);

        if(formApp == null){
            throw new IllegalStateException("Approver not found");
        }
        FormApprover lastApprover = formApproverRepo.findTopByFormIdOrderByApproverOrderDesc(formApp.getFormId());
        if(lastApprover.getApproverOrder() == formApp.getApproverOrder()){
            dynamicFormUserService.acceptForm(formApp.getFormId());
        }
        formApp.setApproverId(user.getId());
        formApp.setApprovalDescription(approvalDTO.getDescription());
        formApp.setApproverStatus("Accepted");
        formApp.setApprovalAt(new Date());
        formApproverRepo.save(formApp);

        return dynamicFormUserService.getTheFormModified(header, approverId);
    }

    public Object rejectForm(Long approverId, ApprovalDTO approvalDTO, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        FormApprover formApp = formApproverRepo.findById(approverId).orElse(null);

        if(formApp == null){
            throw new IllegalStateException("Approver not found");
        }
        dynamicFormUserService.rejectForm(formApp.getFormId());

        formApp.setApproverId(user.getId());
        formApp.setApprovalDescription(approvalDTO.getDescription());
        formApp.setApproverStatus("Rejected");
        formApp.setApprovalAt(new Date());
        formApproverRepo.save(formApp);

        return dynamicFormUserService.getTheFormModified(header, approverId);
    }
}
