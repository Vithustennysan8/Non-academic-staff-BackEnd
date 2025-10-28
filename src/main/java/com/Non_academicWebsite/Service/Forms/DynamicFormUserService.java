package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormFileDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.ApprovalFlow.FormApproverRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormFileDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormUserRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DynamicFormUserService {

    private final DynamicFormUserRepo dynamicFormUserRepo;
    private final ExtractUserService extractUserService;
    private final DynamicFormDetailRepo dynamicFormDetailRepo;
    private final DynamicFormFileDetailRepo dynamicFormFileDetailRepo;
    private final FormApproverRepo formApproverRepo;
    private final UserRepo userRepo;
    private final MailService mailService;
    @Value("${FrontEndURL}")
    private String url;

    public Object getAllFormApplied(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
        List<DynamicFormUser> userAppliedList = dynamicFormUserRepo.findByUserIdOrderByIdDesc(user.getId());
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

    public Object getAllFormAppliedBYId(String id) {
        return dynamicFormUserRepo.findByUserId(id);
    }

    public Object getAllFormRequests(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<FormApprover> approvalForms = formApproverRepo.findByApproverOrderByIdDesc(user.getJobType()) ;

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

    public Object getTheFormModified(String header, Long approverId) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        FormApprover formApprover = formApproverRepo.findById(approverId).orElseThrow( () ->
                new ResourceNotFoundException("Form Approver not found"));

        if(Objects.equals(user.getJobType(), "Head of the Department")){
            List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();
            List<FormApprover> formApprovers = formApproverRepo.findByFormId(formApprover.getFormId());
            Map<String, Object> dynamic = new HashMap<>();
            DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFacultyAndDepartment(formApprover.getFormId(),
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
                    List<FormApprover> formApprovers = formApproverRepo.findByFormId(formApprover.getFormId());
                    Map<String, Object> dynamic = new HashMap<>();
                    DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findByIdAndFaculty(formApprover.getFormId(),
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
                List<FormApprover> formApprovers = formApproverRepo.findByFormId(formApprover.getFormId());
                Map<String, Object> dynamic = new HashMap<>();
                DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(formApprover.getFormId()).orElse(null);
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
        Map<String, Object> allForms = new LinkedHashMap<>();

        for(Map<String, Object> map : list){
            // create a new map to hold the form data and details
            Map<String, Object> form =  new HashMap<>();

            DynamicFormUser dynamicFormUser = (DynamicFormUser) map.get("formDetails");
            List<DynamicFormDetail> details = dynamicFormDetailRepo.findByDynamicFormUserId(dynamicFormUser.getId());
            form.put("formId", dynamicFormUser.getId());
            form.put("form", dynamicFormUser.getDynamicForm().getFormType());
            form.put("formUser", dynamicFormUser.getUser());
            form.put("faculty", dynamicFormUser.getUser().getFaculty());
            form.put("department", dynamicFormUser.getUser().getDepartment());
            form.put("formStatus", dynamicFormUser.getStatus());
            form.put("formCreatedAt", dynamicFormUser.getCreatedAt());
            form.put("formDetails", new ArrayList<Map<String,String>>());
            form.put("approverDetails", map.get("approverDetails"));

            List<Map<String, String>> detail = (List<Map<String, String>>) form.get("formDetails");
            details.forEach(dynamicFormDetail ->
                    detail.add(Map.of(dynamicFormDetail.getTag(), dynamicFormDetail.getValue())));
            allForms.put(dynamicFormUser.getId().toString(), form);
        }
        return allForms;
    }

    public void acceptForm(Long formId, String approverId) throws ResourceNotFoundException {
        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(formId).orElseThrow(() ->
                new ResourceNotFoundException("Form not found for id " + formId));
        User approver = userRepo.findById(approverId).orElseThrow(() ->
                new ResourceNotFoundException("Approver not found for id " + approverId));

        dynamicFormUser.setStatus("Accepted");
        dynamicFormUserRepo.save(dynamicFormUser);
        mailService.sendMail(dynamicFormUser.getUser().getEmail(), url, dynamicFormUser.getUser().getFirst_name(),
                dynamicFormUser.getDynamicForm().getFormType() , "Accepted", approver.getFirst_name());
    }

    public void rejectForm(Long formId, String approverId) throws ResourceNotFoundException {
        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(formId).orElseThrow(() ->
                new ResourceNotFoundException("Form not found for id " + formId));
        User approver = userRepo.findById(approverId).orElseThrow(() ->
                new ResourceNotFoundException("Approver not found for id " + approverId));

        dynamicFormUser.setStatus("Rejected");
        dynamicFormUserRepo.save(dynamicFormUser);
        mailService.sendMail(dynamicFormUser.getUser().getEmail(), url, dynamicFormUser.getUser().getFirst_name(),
                dynamicFormUser.getDynamicForm().getFormType() , "Rejected", approver.getFirst_name());
    }

    public DynamicFormFileDetail generatePdf(Long id) {
        return dynamicFormFileDetailRepo.findByDynamicFormUserId(id);
    }

    @Transactional
    public Object deleteFormByUser(String header, Long id) throws ResourceNotFoundException, UnauthorizedAccessException,
            FormUnderProcessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));

        if(!Objects.equals(dynamicFormUser.getUser().getId(), user.getId())){
            throw new UnauthorizedAccessException("Permission denied");
        }

        FormApprover formApprover = formApproverRepo.findByFormIdAndApproverOrder(dynamicFormUser.getId(), 1);
        if(formApprover == null){
            throw new ResourceNotFoundException("Aprrover flow issue");
        }

        if(Objects.equals(formApprover.getApproverStatus(), "Accepted")){
            throw new FormUnderProcessException("Form under process, can't delete this form");
        }

        formApproverRepo.deleteAllByFormId(id);
        dynamicFormDetailRepo.deleteAllByDynamicFormUserId(dynamicFormUser.getId());
        dynamicFormFileDetailRepo.deleteAllByDynamicFormUserId(dynamicFormUser.getId());
        dynamicFormUserRepo.delete(dynamicFormUser);
        return "Success";
    }

    @Transactional
    public Object deleteForm(String header, Long id) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        DynamicFormUser dynamicFormUser = dynamicFormUserRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));

        if(user.getRole() == Role.USER){
            throw new UnauthorizedAccessException("Permission denied");
        }

        FormApprover formApprover = formApproverRepo.findByFormIdAndApproverOrder(dynamicFormUser.getId(), 1);
        if(formApprover == null){
            throw new ResourceNotFoundException("Aprrover flow issue");
        }

        formApproverRepo.deleteAllByFormId(id);
        dynamicFormDetailRepo.deleteAllByDynamicFormUserId(dynamicFormUser.getId());
        dynamicFormFileDetailRepo.deleteAllByDynamicFormUserId(dynamicFormUser.getId());
        dynamicFormUserRepo.delete(dynamicFormUser);
        return "Success";
    }
}
