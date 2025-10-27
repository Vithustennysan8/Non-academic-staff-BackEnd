package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.TransferFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.Forms.TransferFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TransferFormService {
    @Autowired
    private TransferFormRepo transferFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailService mailService;
    @Autowired
    private ExtractUserService extractUserService;
    private final String url = "http://localhost:5173/notifications";

    public TransferForm add(String header, TransferFormDTO transferFormDTO, MultipartFile file) throws IOException, ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        TransferForm transferForm = TransferForm.builder()
                .designation(transferFormDTO.getDesignation())
                .servicePeriodOfCurrent(transferFormDTO.getServicePeriodOfCurrent())
                .file(file != null ? file.getBytes(): null)
                .fileName(file != null ? file.getOriginalFilename(): null)
                .fileType(file != null ? file.getContentType():null)
                .user(user)
                .currentJobEndDate(transferFormDTO.getCurrentJobEndDate())
                .currentJobStartDate(transferFormDTO.getCurrentJobStartDate())
                .previousJobStartDate(transferFormDTO.getPreviousJobStartDate())
                .previousJobEndDate(transferFormDTO.getPreviousJobEndDate())
                .leaveAt(transferFormDTO.getLeaveAt())
                .leaveDays(transferFormDTO.getLeaveDays())
                .formType("Transfer Form")
                .headStatus("pending")
                .deanStatus("pending")
                .registrarStatus("pending")
                .naeStatus("pending")
                .registrarApprovalStatus("pending")
                .status("Pending")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return transferFormRepo.save(transferForm);
    }

    public List<TransferForm> getTransferForms(ReqFormsDTO reqFormsDTO, String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return transferFormRepo.findByUserIdStartingWith(prefix);
        }
        else if(reqFormsDTO.getFaculty() == null){
            return transferFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        else if(reqFormsDTO.getDepartment() == null){
            return transferFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return transferFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<TransferForm> getForms(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return transferFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<TransferForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return transferFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        TransferForm transferForm = transferFormRepo.findById(formId).orElse(null);
        if(transferForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            switch (job) {
                case "Head of the Department" -> {
                    transferForm.setHeadStatus("Accepted");
                    transferForm.setHead(approvalDTO.getUser());
                    transferForm.setHeadDescription(approvalDTO.getDescription());
                    transferForm.setHeadReactedAt(new Date());
                    return transferFormRepo.save(transferForm);
                }
                case "Dean" -> {
                    transferForm.setDeanStatus("Accepted");
                    transferForm.setDean(approvalDTO.getUser());
                    transferForm.setDeanDescription(approvalDTO.getDescription());
                    transferForm.setDeanReactedAt(new Date());
                    return transferFormRepo.save(transferForm);
                }
                case "Registrar" -> {
                    System.out.println(transferForm.getRegistrarStatus());
                    if(Objects.equals(transferForm.getRegistrarStatus(), "Accepted")){
                        System.out.println("In-- Registrar");
                        transferForm.setRegistrarApprovalStatus("Accepted");
                        transferForm.setRegistrarApproval(approvalDTO.getUser());
                        transferForm.setRegistrarApprovalDescription(approvalDTO.getDescription());
                        transferForm.setRegistrarApprovalAt(new Date());
                        transferForm.setStatus("Accepted");
                        mailService.sendMail(transferForm.getUser().getEmail(), url, transferForm.getUser().getFirst_name(), transferForm.getFormType(), "Accepted", approver.getFirst_name());
                        return transferFormRepo.save(transferForm);
                    }
                    System.out.println("out Registrar");

                    transferForm.setRegistrarStatus("Accepted");
                    transferForm.setRegistrar(approvalDTO.getUser());
                    transferForm.setRegistrarDescription(approvalDTO.getDescription());
                    transferForm.setRegistrarReactedAt(new Date());
                    return transferFormRepo.save(transferForm);
                }
                case "Non Academic Establishment Division" -> {
                    transferForm.setNaeStatus("Accepted");
                    transferForm.setNae(approvalDTO.getUser());
                    transferForm.setNaeDescription(approvalDTO.getDescription());
                    transferForm.setNaeReactedAt(new Date());
                    return transferFormRepo.save(transferForm);
                }
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        TransferForm transferForm = transferFormRepo.findById(formId).orElse(null);
        if(transferForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            switch (job) {
                case "Head of the Department" -> {
                    transferForm.setHeadStatus("Rejected");
                    transferForm.setDean(approvalDTO.getUser());
                    transferForm.setDeanDescription(approvalDTO.getDescription());
                    transferForm.setHeadReactedAt(new Date());
                    transferForm.setStatus("Rejected");
                    mailService.sendMail(transferForm.getUser().getEmail(), url, user.getFirst_name(), transferForm.getFormType() , "Rejected", approver.getFirst_name());
                    return transferFormRepo.save(transferForm);
                }
                case "Dean" -> {
                    transferForm.setDeanStatus("Rejected");
                    transferForm.setDean(approvalDTO.getUser());
                    transferForm.setDeanDescription(approvalDTO.getDescription());
                    transferForm.setDeanReactedAt(new Date());
                    transferForm.setStatus("Rejected");
                    mailService.sendMail(transferForm.getUser().getEmail(), url, user.getFirst_name(), transferForm.getFormType() , "Rejected", approver.getFirst_name());
                    return transferFormRepo.save(transferForm);
                }
                case "Registrar" -> {
                    transferForm.setRegistrarStatus("Rejected");
                    transferForm.setRegistrar(approvalDTO.getUser());
                    transferForm.setRegistrarDescription(approvalDTO.getDescription());
                    transferForm.setRegistrarReactedAt(new Date());
                    transferForm.setStatus("Rejected");
                    mailService.sendMail(transferForm.getUser().getEmail(), url, user.getFirst_name(), transferForm.getFormType() , "Rejected", approver.getFirst_name());
                    return transferFormRepo.save(transferForm);
                }
                case "Non Academic Establishment Division" -> {
                    transferForm.setNaeStatus("Rejected");
                    transferForm.setNae(approvalDTO.getUser());
                    transferForm.setNaeDescription(approvalDTO.getDescription());
                    transferForm.setNaeReactedAt(new Date());
                    transferForm.setStatus("Rejected");
                    mailService.sendMail(transferForm.getUser().getEmail(), url, user.getFirst_name(), transferForm.getFormType() , "Rejected", approver.getFirst_name());
                    return transferFormRepo.save(transferForm);
                }
                case "RegistrarApproval" -> {
                    transferForm.setRegistrarApprovalStatus("Rejected");
                    transferForm.setRegistrarApproval(approvalDTO.getUser());
                    transferForm.setRegistrarApprovalDescription(approvalDTO.getDescription());
                    transferForm.setRegistrarApprovalAt(new Date());
                    transferForm.setStatus("Rejected");
                    mailService.sendMail(transferForm.getUser().getEmail(), url, user.getFirst_name(), transferForm.getFormType(), "Rejected", approver.getFirst_name());
                    return transferFormRepo.save(transferForm);
                }
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId) {
        if(!transferFormRepo.existsByUserId(userId)){
            return "there is no form for this user";
        }
        transferFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) throws FormUnderProcessException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        TransferForm transferForm = transferFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (transferForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), transferForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "SUPER_ADMIN")){
            if(Objects.equals(transferForm.getHeadStatus(), "pending")){
                transferFormRepo.deleteById(id);
                return "Form deleted Successfully";
            }
            throw new FormUnderProcessException("Form is under process, Can't delete!!!");
        }
        return "Form deleted rejected";
    }
}
