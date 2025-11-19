package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormFileDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import com.Non_academicWebsite.Entity.JobPosition;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.Non_academicWebsite.Entity.JobScope.*;

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
    private final JobPositionRepository jobPositionRepository;
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

        // Fetch all approver entries that belong to this job type
        List<FormApprover> approvalForms = formApproverRepo.findByApproverOrderByIdDesc(user.getJobType());
        JobPosition jobPosition = jobPositionRepository.findByJobPositionName(user.getJobType());

        List<Map<String, Object>> dynamicFormUsers = new ArrayList<>();

        for (FormApprover approval : approvalForms) {

            // Check if it is NOW this approver's turn
            if (!isMyTurn(approval)) continue;

            // Find form based on scope (department-level or faculty-level)
            DynamicFormUser formUser = findFormForUserScope(approval.getFormId(), user, jobPosition);

            if (formUser != null) {
                Map<String, Object> dynamic = new HashMap<>();
                List<FormApprover> formApprovers =
                        formApproverRepo.findByFormId(approval.getFormId());

                dynamic.put("formDetails", formUser);
                dynamic.put("approverDetails",
                        formApprovers.stream()
                                .sorted(Comparator.comparingInt(FormApprover::getApproverOrder))
                                .collect(Collectors.toList()));

                dynamicFormUsers.add(dynamic);
            }
        }

        if (dynamicFormUsers.isEmpty()) return Collections.emptyList();

        Map<String, Object> allForms = generateOutput(dynamicFormUsers);
        return allForms.values().toArray();
    }

    private boolean isMyTurn(FormApprover approval) {
        if (approval.getApproverOrder() == 1) return true;

        return formApproverRepo.existsByFormIdAndApproverOrderAndApproverStatus(
                approval.getFormId(),
                approval.getApproverOrder() - 1,
                "Accepted"
        );
    }

    private DynamicFormUser findFormForUserScope(Long formId, User user, JobPosition job) {

        switch (job.getJobScope()) {

            case DEPARTMENT_SCOPE:
                return dynamicFormUserRepo.findByIdAndFacultyAndDepartment(
                        formId,
                        user.getFaculty(),
                        user.getDepartment()
                );

            case FACULTY_SCOPE:
                return dynamicFormUserRepo.findByIdAndFaculty(
                        formId,
                        user.getFaculty()
                );

            case GLOBAL_SCOPE:
                return dynamicFormUserRepo.findById(formId).orElse(null);

            default:
                return null;
        }
    }

    public Object getTheFormModified(String header, Long approverId) throws ResourceNotFoundException {

        User user = extractUserService.extractUserByAuthorizationHeader(header);
        JobPosition jobPosition = jobPositionRepository.findByJobPositionName(user.getJobType());

        FormApprover formApprover = formApproverRepo.findById(approverId).orElseThrow(
                () -> new ResourceNotFoundException("Form Approver not found")
        );

        Long formId = formApprover.getFormId();

        // Fetch the form belonging to the user based on role scope
        DynamicFormUser formUser = findFormForUserScope(formId, user, jobPosition);

        if (formUser == null) {
            return Collections.emptyList();
        }

        // Get approver chain
        List<FormApprover> formApprovers = formApproverRepo
                .findByFormId(formId)
                .stream()
                .sorted(Comparator.comparingInt(FormApprover::getApproverOrder))
                .collect(Collectors.toList());

        // Build output
        Map<String, Object> dynamic = new HashMap<>();
        dynamic.put("formDetails", formUser);
        dynamic.put("approverDetails", formApprovers);

        Map<String, Object> result = generateOutput(Collections.singletonList(dynamic));

        return result.values().toArray();
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
