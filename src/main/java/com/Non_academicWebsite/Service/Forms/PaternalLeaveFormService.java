package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.PaternalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.Forms.PaternalLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.PaternalLeaveFormRepo;
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
public class PaternalLeaveFormService {
    @Autowired
    private PaternalLeaveFormRepo paternalLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public PaternalLeaveForm add(String header, PaternalLeaveFormDTO paternalLeaveFormDTO, MultipartFile file) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        PaternalLeaveForm paternalLeaveForm = PaternalLeaveForm.builder()
                .childBirthDate(paternalLeaveFormDTO.getChildBirthDate())
                .designation(paternalLeaveFormDTO.getDesignation())
                .requestedDate(paternalLeaveFormDTO.getRequestedDate())
                .file(file.getBytes())
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .user(user)
                .formType("Paternal Leave Form")
                .headStatus("pending")
                .deanStatus("pending")
                .naeStatus("pending")
                .status("Pending")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return paternalLeaveFormRepo.save(paternalLeaveForm);
    }

    public List<PaternalLeaveForm> getPaternalLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return paternalLeaveFormRepo.findByUserIdStartingWith(prefix);
        }
        else if(reqFormsDTO.getFaculty() == null){
            return paternalLeaveFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        else if(reqFormsDTO.getDepartment() == null){
            return paternalLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return paternalLeaveFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<PaternalLeaveForm> getForms(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return paternalLeaveFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<PaternalLeaveForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return paternalLeaveFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        PaternalLeaveForm paternalLeaveForm = paternalLeaveFormRepo.findById(formId).orElse(null);
        if(paternalLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                paternalLeaveForm.setHeadStatus("Accepted");
                paternalLeaveForm.setHead(approvalDTO.getUser());
                paternalLeaveForm.setHeadDescription(approvalDTO.getDescription());
                paternalLeaveForm.setHeadReactedAt(new Date());
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            } else if (job.equals("Dean")) {
                paternalLeaveForm.setDeanStatus("Accepted");
                paternalLeaveForm.setDean(approvalDTO.getUser());
                paternalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                paternalLeaveForm.setDeanReactedAt(new Date());
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            } else if (job.equals("Non Academic Establishment Division")) {
                paternalLeaveForm.setNaeStatus("Accepted");
                paternalLeaveForm.setNae(approvalDTO.getUser());
                paternalLeaveForm.setNaeDescription(approvalDTO.getDescription());
                paternalLeaveForm.setNaeReactedAt(new Date());
                paternalLeaveForm.setStatus("Accepted");
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        PaternalLeaveForm paternalLeaveForm = paternalLeaveFormRepo.findById(formId).orElse(null);
        if(paternalLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                paternalLeaveForm.setHeadStatus("Rejected");
                paternalLeaveForm.setDean(approvalDTO.getUser());
                paternalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                paternalLeaveForm.setHeadReactedAt(new Date());
                paternalLeaveForm.setStatus("Rejected");
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            }else if (job.equals("Dean")) {
                paternalLeaveForm.setDeanStatus("Rejected");
                paternalLeaveForm.setDean(approvalDTO.getUser());
                paternalLeaveForm.setDeanDescription(approvalDTO.getDescription());
                paternalLeaveForm.setDeanReactedAt(new Date());
                paternalLeaveForm.setStatus("Rejected");
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            }else if (job.equals("Non Academic Establishment Division")) {
                paternalLeaveForm.setNaeStatus("Rejected");
                paternalLeaveForm.setNae(approvalDTO.getUser());
                paternalLeaveForm.setNaeDescription(approvalDTO.getDescription());
                paternalLeaveForm.setNaeReactedAt(new Date());
                paternalLeaveForm.setStatus("Rejected");
                return paternalLeaveFormRepo.save(paternalLeaveForm);
            }
        }
        return "Failed";
    }

    public Object deleteForm(String userId){
        if(!paternalLeaveFormRepo.existsByUserId(userId)){
            return "Delete failed, User not found";
        }
        paternalLeaveFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        PaternalLeaveForm paternalLeaveForm = paternalLeaveFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (paternalLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), paternalLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "ADMIN")){
            paternalLeaveFormRepo.deleteById(id);
            return "Form deleted Successfully";
        }
        return "Form deleted rejected";
    }

}
