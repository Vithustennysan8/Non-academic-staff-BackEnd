package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.MaternityLeaveFormDTO;
import com.Non_academicWebsite.DTO.Forms.MedicalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.MaternityLeaveFormRepo;
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
public class MaternityLeaveFormService {
    @Autowired
    private MaternityLeaveFormRepo maternityLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public MaternityLeaveForm add(String header, MaternityLeaveFormDTO medicalLeaveFormDTO, MultipartFile file) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MaternityLeaveForm maternityLeaveForm = MaternityLeaveForm.builder()
                .designation(medicalLeaveFormDTO.getDesignation())
                .childBirthDate(medicalLeaveFormDTO.getChildBirthDate())
                .file(file.getBytes())
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
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
            String job = user.getJob_type();

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
            String job = user.getJob_type();

            switch (job) {
                case "Head of the Department" -> {
                    maternityLeaveForm.setHeadStatus("Rejected");
                    maternityLeaveForm.setDean(approvalDTO.getUser());
                    maternityLeaveForm.setDeanDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setHeadReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Dean" -> {
                    maternityLeaveForm.setDeanStatus("Rejected");
                    maternityLeaveForm.setDean(approvalDTO.getUser());
                    maternityLeaveForm.setDeanDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setDeanReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Chief Medical Officer" -> {
                    maternityLeaveForm.setCmoStatus("Rejected");
                    maternityLeaveForm.setCmo(approvalDTO.getUser());
                    maternityLeaveForm.setCmoDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setCmoReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Registrar" -> {
                    maternityLeaveForm.setRegistrarStatus("Rejected");
                    maternityLeaveForm.setRegistrar(approvalDTO.getUser());
                    maternityLeaveForm.setRegistrarDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setRegistrarReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
                case "Non Academic Establishment Division" -> {
                    maternityLeaveForm.setNaeStatus("Rejected");
                    maternityLeaveForm.setNae(approvalDTO.getUser());
                    maternityLeaveForm.setNaeDescription(approvalDTO.getDescription());
                    maternityLeaveForm.setNaeReactedAt(new Date());
                    maternityLeaveForm.setStatus("Rejected");
                    return maternityLeaveFormRepo.save(maternityLeaveForm);
                }
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId){
        if(!maternityLeaveFormRepo.existsByUserId(userId)){
            return "Delete failed, User not found";
        }
        maternityLeaveFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        MaternityLeaveForm maternityLeaveForm = maternityLeaveFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (maternityLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), maternityLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "ADMIN")){
            maternityLeaveFormRepo.deleteById(id);
            return "Form deleted Successfully";
        }
        return "Form deleted rejected";
    }

}
