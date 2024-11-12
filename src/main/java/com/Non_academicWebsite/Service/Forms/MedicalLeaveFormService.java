package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.MedicalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.Forms.MedicalLeaveFromRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MedicalLeaveFormService {
    @Autowired
    private MedicalLeaveFromRepo medicalLeaveFromRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailService mailService;
    private final String url = "http://localhost:5173/notifications";

    public MedicalLeaveForm add(String header, MedicalLeaveFormDTO medicalLeaveFormDTO, MultipartFile file) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MedicalLeaveForm medicalLeaveForm = MedicalLeaveForm.builder()
                .designation(medicalLeaveFormDTO.getDesignation())
                .requestPeriodStart(medicalLeaveFormDTO.getRequestPeriodStart())
                .requestPeriodEnd(medicalLeaveFormDTO.getRequestPeriodEnd())
                .file(file != null ? file.getBytes(): null)
                .fileName(file != null ? file.getOriginalFilename(): null)
                .fileType(file != null ? file.getContentType(): null)
                .leaveAt(medicalLeaveFormDTO.getRequestPeriodStart())
                .leaveDays(medicalLeaveFormDTO.getLeaveDays())
                .user(user)
                .formType("Medical Leave Form")
                .headStatus("pending")
                .deanStatus("pending")
                .naeStatus("pending")
                .cmoStatus("pending")
                .registrarStatus("pending")
                .status("Pending")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return medicalLeaveFromRepo.save(medicalLeaveForm);
    }

    public List<MedicalLeaveForm> getMedicalLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return medicalLeaveFromRepo.findByUserIdStartingWith(prefix);
        }
        else if(reqFormsDTO.getFaculty() == null){
            System.out.println("CLick");
            return medicalLeaveFromRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        else if(reqFormsDTO.getDepartment() == null){
            return medicalLeaveFromRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return medicalLeaveFromRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<MedicalLeaveForm> getForms(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return medicalLeaveFromRepo.findByUserIdStartingWith(prefix);
    }

    public List<MedicalLeaveForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return medicalLeaveFromRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        MedicalLeaveForm medicalLeaveForm = medicalLeaveFromRepo.findById(formId).orElse(null);
        if(medicalLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                medicalLeaveForm.setHeadStatus("Accepted");
                medicalLeaveForm.setHead(approvalDTO.getUser());
                medicalLeaveForm.setHeadDescription(approvalDTO.getDescription());
                medicalLeaveForm.setHeadReactedAt(new Date());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            } else if (job.equals("Dean")) {
                medicalLeaveForm.setDeanStatus("Accepted");
                medicalLeaveForm.setDean(approvalDTO.getUser());
                medicalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                medicalLeaveForm.setDeanReactedAt(new Date());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Chief Medical Officer")) {
                medicalLeaveForm.setCmoStatus("Accepted");
                medicalLeaveForm.setCmo(approvalDTO.getUser());
                medicalLeaveForm.setCmoDescription(approvalDTO.getDescription());
                medicalLeaveForm.setCmoReactedAt(new Date());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Registrar")) {
                medicalLeaveForm.setRegistrarStatus("Accepted");
                medicalLeaveForm.setRegistrar(approvalDTO.getUser());
                medicalLeaveForm.setRegistrarDescription(approvalDTO.getDescription());
                medicalLeaveForm.setRegistrarReactedAt(new Date());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Non Academic Establishment Division")) {
                medicalLeaveForm.setNaeStatus("Accepted");
                medicalLeaveForm.setNae(approvalDTO.getUser());
                medicalLeaveForm.setNaeDescription(approvalDTO.getDescription());
                medicalLeaveForm.setNaeReactedAt(new Date());
                medicalLeaveForm.setStatus("Accepted");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Accepted", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        MedicalLeaveForm medicalLeaveForm = medicalLeaveFromRepo.findById(formId).orElse(null);
        if(medicalLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                medicalLeaveForm.setHeadStatus("Rejected");
                medicalLeaveForm.setDean(approvalDTO.getUser());
                medicalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                medicalLeaveForm.setHeadReactedAt(new Date());
                medicalLeaveForm.setStatus("Rejected");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Dean")) {
                medicalLeaveForm.setDeanStatus("Rejected");
                medicalLeaveForm.setDean(approvalDTO.getUser());
                medicalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                medicalLeaveForm.setDeanReactedAt(new Date());
                medicalLeaveForm.setStatus("Rejected");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Chief Medical Officer")) {
                medicalLeaveForm.setCmoStatus("Rejected");
                medicalLeaveForm.setCmo(approvalDTO.getUser());
                medicalLeaveForm.setCmoDescription(approvalDTO.getDescription());
                medicalLeaveForm.setCmoReactedAt(new Date());
                medicalLeaveForm.setStatus("Rejected");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Registrar")) {
                medicalLeaveForm.setRegistrarStatus("Rejected");
                medicalLeaveForm.setRegistrar(approvalDTO.getUser());
                medicalLeaveForm.setRegistrarDescription(approvalDTO.getDescription());
                medicalLeaveForm.setRegistrarReactedAt(new Date());
                medicalLeaveForm.setStatus("Rejected");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }else if (job.equals("Non Academic Establishment Division")) {
                medicalLeaveForm.setNaeStatus("Rejected");
                medicalLeaveForm.setNae(approvalDTO.getUser());
                medicalLeaveForm.setNaeDescription(approvalDTO.getDescription());
                medicalLeaveForm.setNaeReactedAt(new Date());
                medicalLeaveForm.setStatus("Rejected");
                mailService.sendMail(medicalLeaveForm.getUser().getEmail(), url, medicalLeaveForm.getUser().getFirst_name(), medicalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return medicalLeaveFromRepo.save(medicalLeaveForm);
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId) {
        if(!medicalLeaveFromRepo.existsByUserId(userId)){
            return "There is no user with this userId";
        }
        medicalLeaveFromRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) throws FormUnderProcessException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MedicalLeaveForm medicalLeaveForm = medicalLeaveFromRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (medicalLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), medicalLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "SUPER_ADMIN")){
            if(medicalLeaveForm.getHeadStatus() == "pending"){
                medicalLeaveFromRepo.deleteById(id);
                return "Form deleted Successfully";
            }
            throw new FormUnderProcessException("Form is under process, Can't delete!!!");
        }
        return "Form deleted rejected";
    }

}
