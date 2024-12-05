package com.Non_academicWebsite.Service.ApprovalFlow;

import com.Non_academicWebsite.Entity.ApprovalFlow.ApprovalFlow;
import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ApprovalFlow.ApprovalFlowRepo;
import com.Non_academicWebsite.Repository.ApprovalFlow.FormApproverRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormApproverService {

    @Autowired
    private FormApproverRepo formApproverRepo;
    @Autowired
    private ExtractUserService extractUserService;

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
}
