package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.MaternityLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.Forms.MaternityLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class MaternityLeaveFormService {
    @Autowired
    private MaternityLeaveFormRepo maternityLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailService mailService;
    private final String url = "http://localhost:5173/notifications";

    public MaternityLeaveForm add(String header, MaternityLeaveFormDTO maternityDTO, MultipartFile file) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MaternityLeaveForm maternityLeaveForm = MaternityLeaveForm.builder()
                .designation(maternityDTO.getDesignation())
                .childBirthDate(maternityDTO.getChildBirthDate())
                .file(file != null ? file.getBytes(): null)
                .fileName(file != null ? file.getOriginalFilename(): null)
                .fileType(file != null ? file.getContentType():null)
                .leaveAt(maternityDTO.getLeaveAt())
                .leaveDays(maternityDTO.getLeaveDays())
                .user(user)
                .formType("Maternity Leave Form")
                .headStatus("pending")
                .deanStatus("pending")
                .naeStatus("pending")
                .cmoStatus("pending")
                .registrarStatus("pending")
                .status("Pending")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return maternityLeaveFormRepo.save(maternityLeaveForm);
    }

    public List<MaternityLeaveForm> getMaternityLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return maternityLeaveFormRepo.findByUserIdStartingWith(prefix);
        }
        else if(reqFormsDTO.getFaculty() == null){
            return maternityLeaveFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        else if(reqFormsDTO.getDepartment() == null){
            return maternityLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return maternityLeaveFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<MaternityLeaveForm> getForms(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return maternityLeaveFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<MaternityLeaveForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return maternityLeaveFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        MaternityLeaveForm maternityLeaveForm = maternityLeaveFormRepo.findById(formId).orElse(null);
        if(maternityLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            switch (job) {
                case "Head of the Department" -> {
                    maternityLeaveForm.setHeadStatus("Accepted");
                    maternityLeaveForm.setHead(approvalDTO.getUser());
                    maternityLeaveForm.setHeadDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setHeadReactedAt(new Date());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Dean" -> {
                    maternityLeaveForm.setDeanStatus("Accepted");
                    maternityLeaveForm.setDean(approvalDTO.getUser());
                    maternityLeaveForm.setDeanDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setDeanReactedAt(new Date());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Chief Medical Officer" -> {
                    maternityLeaveForm.setCmoStatus("Accepted");
                    maternityLeaveForm.setCmo(approvalDTO.getUser());
                    maternityLeaveForm.setCmoDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setCmoReactedAt(new Date());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Registrar" -> {
                    maternityLeaveForm.setRegistrarStatus("Accepted");
                    maternityLeaveForm.setRegistrar(approvalDTO.getUser());
                    maternityLeaveForm.setRegistrarDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setRegistrarReactedAt(new Date());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Non Academic Establishment Division" -> {
                    maternityLeaveForm.setNaeStatus("Accepted");
                    maternityLeaveForm.setNae(approvalDTO.getUser());
                    maternityLeaveForm.setNaeDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setNaeReactedAt(new Date());
                    maternityLeaveForm.setStatus("Accepted");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Accepted", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        MaternityLeaveForm maternityLeaveForm = maternityLeaveFormRepo.findById(formId).orElse(null);
        if(maternityLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            switch (job) {
                case "Head of the Department" -> {
                    maternityLeaveForm.setHeadStatus("Rejected");
                    maternityLeaveForm.setDean(approvalDTO.getUser());
                    maternityLeaveForm.setDeanDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setHeadReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Dean" -> {
                    maternityLeaveForm.setDeanStatus("Rejected");
                    maternityLeaveForm.setDean(approvalDTO.getUser());
                    maternityLeaveForm.setDeanDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setDeanReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Chief Medical Officer" -> {
                    maternityLeaveForm.setCmoStatus("Rejected");
                    maternityLeaveForm.setCmo(approvalDTO.getUser());
                    maternityLeaveForm.setCmoDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setCmoReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Registrar" -> {
                    maternityLeaveForm.setRegistrarStatus("Rejected");
                    maternityLeaveForm.setRegistrar(approvalDTO.getUser());
                    maternityLeaveForm.setRegistrarDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setRegistrarReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Non Academic Establishment Division" -> {
                    maternityLeaveForm.setNaeStatus("Rejected");
                    maternityLeaveForm.setNae(approvalDTO.getUser());
                    maternityLeaveForm.setNaeDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setNaeReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    mailService.sendMail(maternityLeaveForm.getUser().getEmail(), url, maternityLeaveForm.getUser().getFirst_name(), maternityLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId) {
        if(!maternityLeaveFormRepo.existsByUserId(userId)){
            return "there is no form for this user";
        }
        maternityLeaveFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) throws FormUnderProcessException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MaternityLeaveForm maternityLeaveForm = maternityLeaveFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (maternityLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), maternityLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "SUPER_ADMIN")){
            if(Objects.equals(maternityLeaveForm.getHeadStatus(), "pending")){
                maternityLeaveFormRepo.deleteById(id);
                return "Form deleted Successfully";
            }
            throw new FormUnderProcessException("Form is under process, Can't delete!!!");
        }
        return "Form deleted rejected";
    }

    public Collection<MaternityLeaveForm> getFormsOfUserById(String id) {
        return maternityLeaveFormRepo.findByUserId(id);
    }
}
