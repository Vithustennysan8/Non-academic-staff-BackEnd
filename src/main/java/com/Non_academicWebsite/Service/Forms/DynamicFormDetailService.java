package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.PartialFileUploadException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DynamicFormDetailService {

    private final DynamicFormDetailRepo dynamicFormDetailRepo;
    private final ExtractUserService extractUserService;
    private final DynamicFormService dynamicFormService;
    private final DynamicFormUserRepo dynamicFormUserRepo;
    private final DynamicFormFileDetailRepo dynamicFormFileDetailRepo;
    private final ApprovalFlowRepo approvalFlowRepo;
    private final FormApproverRepo formApproverRepo;


    public Object addDynamicFormDetails(String header, Map<String, String> parameters, List<MultipartFile> files,
                                        String form, String flow)
            throws UnauthorizedAccessException, ResourceNotFoundException, PartialFileUploadException {
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
                .createdAt(LocalDateTime.now())
                .build();
        dynamicFormUserRepo.save(dynamicFormUser);

        List<ApprovalFlow> flows = approvalFlowRepo.findByUniqueNameAndDynamicFormAndDepartmentAndFaculty(flow,
                dynamicForm.getFormType(), user.getDepartment(), user.getFaculty());

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

        List<String> failedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                DynamicFormFileDetail detail = DynamicFormFileDetail.builder()
                        .dynamicFormUser(dynamicFormUser)
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .file(file.getBytes())
                        .build();

                dynamicFormFileDetailRepo.save(detail);
            } catch (IOException e) {
                failedFiles.add(file.getOriginalFilename());
            }
        }

        if (!failedFiles.isEmpty()) {
            throw new PartialFileUploadException("Some files failed: " + String.join(", ", failedFiles));
        }
        return "Added dynamic form";
    }
}
