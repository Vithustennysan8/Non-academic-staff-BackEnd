package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.DynamicFormNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Entity.ApprovalFlow.ApprovalFlow;
import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormFileDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ApprovalFlow.ApprovalFlowRepo;
import com.Non_academicWebsite.Repository.ApprovalFlow.FormApproverRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormFileDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormUserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class DynamicFormDetailService {
    @Autowired
    private DynamicFormDetailRepo dynamicFormDetailRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private DynamicFormService dynamicFormService;
    @Autowired
    private DynamicFormUserRepo dynamicFormUserRepo;
    @Autowired
    private DynamicFormUserService dynamicFormUserService;
    @Autowired
    private DynamicFormFileDetailRepo dynamicFormFileDetailRepo;
    @Autowired
    private ApprovalFlowRepo approvalFlowRepo;
    @Autowired
    private FormApproverRepo formApproverRepo;


    public Object addDynamicFormDetails(String header, Map<String, String> parameters, List<MultipartFile> files,
                                        String form, String flow) throws DynamicFormNotFoundException, UnauthorizedAccessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if (user.getRole() != Role.USER){
            throw new UnauthorizedAccessException("User only can requests leaves!");
        }

        // save the form and user details
        DynamicForm dynamicForm = dynamicFormService.getForm(form, user.getDepartment(), user.getFaculty());
        DynamicFormUser dynamicFormUser = DynamicFormUser.builder()
                .dynamicForm(dynamicForm)
                .user(user)
                .status("Pending")
                .createdAt(new Date())
                .build();
        dynamicFormUserRepo.save(dynamicFormUser);

        List<ApprovalFlow> flows = approvalFlowRepo.findByUniqueNameAndDepartmentAndFaculty(flow, user.getDepartment(),
                user.getFaculty());

        flows.forEach(Flow -> {
            FormApprover formApprover = FormApprover.builder()
                    .formId(dynamicFormUser.getId())
                    .formFlowType(flow)
                    .approver(Flow.getRoleName())
                    .approverOrder(Flow.getSequence())
                    .approverStatus("Pending")
                    .build();
            formApproverRepo.save(formApprover);
        });

        // save the specific form fields and values
        for( Map.Entry<String,String> entry : parameters.entrySet()) {
            if(entry.getValue() == null){continue;}
            DynamicFormDetail dynamicFormDetail = DynamicFormDetail.builder()
                    .dynamicFormUser(dynamicFormUser)
                    .tag(entry.getKey())
                    .value(entry.getValue())
                    .build();
            dynamicFormDetailRepo.save(dynamicFormDetail);
        }

        // save the specific form multipart files input
        if(files == null){
            return "form submitted without multiple files";
        }
        files.forEach(file  -> {
            try {
                DynamicFormFileDetail dynamicFormDetail = DynamicFormFileDetail.builder()
                        .dynamicFormUser(dynamicFormUser)
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .file(file.getBytes())
                        .build();
                dynamicFormFileDetailRepo.save(dynamicFormDetail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return "Added dynamic form";
    }
}
