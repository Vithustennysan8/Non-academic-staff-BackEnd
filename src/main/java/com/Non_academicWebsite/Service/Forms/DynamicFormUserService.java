package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ApprovalFlow.FormApproverRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormUserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamicFormUserService {

    @Autowired
    private DynamicFormUserRepo dynamicFormUserRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private DynamicFormDetailRepo dynamicFormDetailRepo;
    @Autowired
    private FormApproverRepo formApproverRepo;

    public Object getAllFormApplied(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
        List<DynamicFormUser> userAppliedList = dynamicFormUserRepo.findByUserId(user.getId());
        if (userAppliedList == null) {return Collections.emptyList();}
        userAppliedList.forEach(userApplied -> {
            List<FormApprover> formApprovers = formApproverRepo.findByFormId(userApplied.getId());
            Map<String, Object> detail = new HashMap<>();
            detail.put("formDetails", userApplied);
            detail.put("approverDetails", formApprovers.stream().sorted(
                    Comparator.comparingInt(FormApprover::getApproverOrder)
                    ).collect(Collectors.toList())
            );
            dynamicFormUsers.add(detail);
        });

        Map<String, Object> allForms = generateOutput(dynamicFormUsers);
        return allForms.values().toArray();
    }

    public Object getAllFormRequests(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<FormApprover> approvalForms = formApproverRepo.findByApprover(user.getJobType()) ;

        if(Objects.equals(user.getJobType(), "Head of the Department")){
            List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
            approvalForms.forEach(approval -> {
                if(approval.getApproverOrder() > 1 && formApproverRepo.existsByFormIdAndApproverOrderAndApproverStatus
                        (approval.getFormId(), approval.getApproverOrder()-1, "Accepted")){

                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(approval.getFormId(),
                            user.getFaculty(), user.getDepartment());
                    if(dynamicFormUser != null){
                        dynamic.put("formDetails", dynamicFormUser);
                        dynamic.put("approverDetails", formApprovers.stream().sorted(
                                Comparator.comparingInt(FormApprover::getApproverOrder)
                                ).collect(Collectors.toList())
                        );
                        dynamicFormUsers.add(dynamic);
                    }
                }else if(approval.getApproverOrder() == 1){
                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(approval.getFormId(),
                            user.getFaculty(), user.getDepartment());
                    if(dynamicFormUser != null){
                        dynamic.put("formDetails", dynamicFormUser);
                        dynamic.put("approverDetails", formApprovers.stream().sorted(
                                        Comparator.comparingInt(FormApprover::getApproverOrder)
                                ).collect(Collectors.toList())
                        );
                        dynamicFormUsers.add(dynamic);
                    }
                }
            });
            if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
            Map<String, Object> allForms = generateOutput(dynamicFormUsers);
            return allForms.values().toArray();

        }else if(Objects.equals(user.getJobType(), "Dean")){
            List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
            approvalForms.forEach(approval -> {
                if(approval.getApproverOrder() > 1 && formApproverRepo.existsByFormIdAndApproverOrderAndApproverStatus
                            (approval.getFormId(), approval.getApproverOrder()-1, "Accepted")) {

                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFaculty(approval.getFormId(),
                            user.getFaculty());
                    if (dynamicFormUser != null) {
                        dynamic.put("formDetails", dynamicFormUser);
                        dynamic.put("approverDetails", formApprovers.stream().sorted(
                                Comparator.comparingInt(FormApprover::getApproverOrder)).collect(Collectors.toList())
                        );
                        dynamicFormUsers.add(dynamic);
                    }
                }else if(approval.getApproverOrder() == 1){
                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(approval.getFormId(),
                            user.getFaculty(), user.getDepartment());
                    if(dynamicFormUser != null){
                        dynamic.put("formDetails", dynamicFormUser);
                        dynamic.put("approverDetails", formApprovers.stream().sorted(
                                        Comparator.comparingInt(FormApprover::getApproverOrder)
                                ).collect(Collectors.toList())
                        );
                        dynamicFormUsers.add(dynamic);
                    }
                }
            });
            if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
            Map<String, Object> allForms = generateOutput(dynamicFormUsers);
            return allForms.values().toArray();
        }

        List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
        approvalForms.forEach(approval -> {
            if(approval.getApproverOrder() > 1 && formApproverRepo.existsByFormIdAndApproverOrderAndApproverStatus
                    (approval.getFormId(), approval.getApproverOrder()-1, "Accepted")) {
                List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
                Map<String, Object> dynamic = new HashMap<>();
                DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(approval.getFormId()).orElse(null);
                if (dynamicFormUser != null) {
                    dynamic.put("formDetails", dynamicFormUser);
                    dynamic.put("approverDetails", formApprovers.stream().sorted(
                            Comparator.comparingInt(FormApprover::getApproverOrder)
                    ).collect(Collectors.toList()
                    ));
                    dynamicFormUsers.add(dynamic);
                }
            }else if(approval.getApproverOrder() == 1){
            List<FormApprover> formApprovers = formApproverRepo.findByFormId(approval.getFormId());
            Map<String, Object> dynamic = new HashMap<>();
            DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(approval.getFormId(),
                    user.getFaculty(), user.getDepartment());
            if(dynamicFormUser != null){
                dynamic.put("formDetails", dynamicFormUser);
                dynamic.put("approverDetails", formApprovers.stream().sorted(
                                Comparator.comparingInt(FormApprover::getApproverOrder)
                        ).collect(Collectors.toList())
                );
                dynamicFormUsers.add(dynamic);
            }
            }
        });
        if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
        Map<String, Object> allForms = generateOutput(dynamicFormUsers);
        return allForms.values().toArray();

    }

    public Object getTheFormModified(String header, Long approverId){
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        FormApprover approvalForm = formApproverRepo.findById(approverId).orElse(null);

        if (approvalForm == null){
            throw new RuntimeException("Form Approver not found");
        }

        if(Objects.equals(user.getJobType(), "Head of the Department")){
            List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
            List<FormApprover> formApprovers = formApproverRepo.findByFormId(approvalForm.getFormId());
            Map<String, Object> dynamic = new HashMap<>();
            DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(approvalForm.getFormId(),
                    user.getFaculty(), user.getDepartment());
            if(dynamicFormUser != null){
                dynamic.put("formDetails", dynamicFormUser);
                dynamic.put("approverDetails", formApprovers.stream().sorted(
                                Comparator.comparingInt(FormApprover::getApproverOrder)
                        ).collect(Collectors.toList())
                );
                dynamicFormUsers.add(dynamic);
            }

            if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
            Map<String, Object> allForms = generateOutput(dynamicFormUsers);
            return allForms.values().toArray();

        }else if(Objects.equals(user.getJobType(), "Dean")){
            List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(approvalForm.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFaculty(approvalForm.getFormId(),
                            user.getFaculty());
                    if (dynamicFormUser != null) {
                        dynamic.put("formDetails", dynamicFormUser);
                        dynamic.put("approverDetails", formApprovers.stream().sorted(
                                Comparator.comparingInt(FormApprover::getApproverOrder)).collect(Collectors.toList())
                        );
                        dynamicFormUsers.add(dynamic);
                    }
            if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
            Map<String, Object> allForms = generateOutput(dynamicFormUsers);
            return allForms.values().toArray();
        }

        List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
                List<FormApprover> formApprovers = formApproverRepo.findByFormId(approvalForm.getFormId());
                Map<String, Object> dynamic = new HashMap<>();
                DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(approvalForm.getFormId()).orElse(null);
                if (dynamicFormUser != null) {
                    dynamic.put("formDetails", dynamicFormUser);
                    dynamic.put("approverDetails", formApprovers.stream().sorted(
                            Comparator.comparingInt(FormApprover::getApproverOrder)
                    ).collect(Collectors.toList()
                    ));
                    dynamicFormUsers.add(dynamic);
                }
        if(dynamicFormUsers.isEmpty()){return Collections.emptyList();}
        Map<String, Object> allForms = generateOutput(dynamicFormUsers);
        return allForms.values().toArray();
    }

    public Map<String, Object> generateOutput(List<Map<String, Object>> list) {
        Map<String, Object> allForms = new HashMap<>();

        for(Map<String, Object> map : list){
            // create a new map to hold the form data and details
            Map<String, Object> form =  new HashMap<>();

            DynamicFormUser dynamicFormUser = (DynamicFormUser) map.get("formDetails");
            List<DynamicFormDetail> details = dynamicFormDetailRepo.findByDynamicFormUserId(dynamicFormUser.getId());
            form.put("formId", dynamicFormUser.getId());
            form.put("form", dynamicFormUser.getDynamicForm().getFormType());
            form.put("userName", dynamicFormUser.getUser().getFirst_name());
            form.put("faculty", dynamicFormUser.getUser().getFaculty());
            form.put("department", dynamicFormUser.getUser().getDepartment());
            form.put("formStatus", dynamicFormUser.getStatus());
            form.put("formCreatedAt", dynamicFormUser.getCreatedAt());
            form.put("formDetails", new ArrayList<Map<String,String>>());
            form.put("approverDetails", map.get("approverDetails"));

            List<Map<String, String>> detail = (List<Map<String, String>>) form.get("formDetails");
            details.forEach(dynamicFormDetail ->
                    detail.add(Map.of(dynamicFormDetail.getTag(), dynamicFormDetail.getValue())));
            allForms.put(dynamicFormUser.getCreatedAt().toString(), form);
        }
        return allForms;
    }

    public void acceptForm(Long formId) {
        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(formId).orElseThrow(() ->
                new NullPointerException("Form not found for id " + formId));

        dynamicFormUser.setStatus("Accepted");
        dynamicFormUserRepo.save(dynamicFormUser);
    }

    public void rejectForm(Long formId) {
        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(formId).orElseThrow(() ->
                new NullPointerException("Form not found for id " + formId));

        dynamicFormUser.setStatus("Rejected");
        dynamicFormUserRepo.save(dynamicFormUser);
    }
}
