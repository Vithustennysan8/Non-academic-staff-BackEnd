package com.Non_academicWebsite.Service.ApprovalFlow;

import com.Non_academicWebsite.Repository.ApprovalFlow.ApprovalFlowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormApproverService {

    @Autowired
    private ApprovalFlowRepo approvalFlowRepo;


}
