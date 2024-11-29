package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.Forms.AccidentLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AccidentLeaveFormService {
    @Autowired
    private AccidentLeaveFormRepo accidentLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailService mailService;
    private final String url = "http://localhost:5173/notifications";


    public AccidentLeaveForm add(String header, AccidentLeaveFormDTO accidentLeaveFormDTO, MultipartFile file) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .accidentOccurredDuring(accidentLeaveFormDTO.getAccidentOccurredDuring())
                .dateAndTimeOfAccident(accidentLeaveFormDTO.getDateAndTimeOfAccident())
                .placeOFAccident(accidentLeaveFormDTO.getPlaceOFAccident())
                .whilePerformingAnyDuty(accidentLeaveFormDTO.getWhilePerformingAnyDuty())
                .natureOfDanger(accidentLeaveFormDTO.getNatureOfDanger())
                .whoInspectTheAccident(accidentLeaveFormDTO.getWhoInspectTheAccident())
                .whoInformedAfterAccident(accidentLeaveFormDTO.getWhoInformedAfterAccident())
                .referralForTreatment(accidentLeaveFormDTO.getReferralForTreatment())
                .dateAndTimeOfReport(accidentLeaveFormDTO.getDateAndTimeOfReport())
                .durationOfHospitalStay(accidentLeaveFormDTO.getDurationOfHospitalStay())
                .isPoliceComplaint(accidentLeaveFormDTO.getIsPoliceComplaint())
                .expectAccidentCompensation(accidentLeaveFormDTO.getExpectAccidentCompensation())
                .file(file != null ? file.getBytes(): null)
                .fileName(file != null ? file.getOriginalFilename(): null)
                .fileType(file != null ? file.getContentType():null)
                .leaveAt(accidentLeaveFormDTO.getLeaveAt())
                .leaveDays(accidentLeaveFormDTO.getLeaveDays())
                .user(user)
                .formType("Accident Leave Form")
                .headStatus("pending")
                .deanStatus("pending")
                .cmoStatus("pending")
                .naeStatus("pending")
                .status("Pending")
                .createdAt(new Date())
                .build();

        return accidentLeaveFormRepo.save(accidentLeaveForm);
    }

    public List<AccidentLeaveForm> getAccidentLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return accidentLeaveFormRepo.findByUserIdStartingWith(prefix);
        }
        else if(reqFormsDTO.getFaculty() == null){
            return accidentLeaveFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        else if(reqFormsDTO.getDepartment() == null){
            return accidentLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return accidentLeaveFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<AccidentLeaveForm> getForms(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return accidentLeaveFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<AccidentLeaveForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return accidentLeaveFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        AccidentLeaveForm accidentLeaveForm = accidentLeaveFormRepo.findById(formId).orElse(null);
        if(accidentLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            if (job.equals("Head of the Department")) {
                accidentLeaveForm.setHeadStatus("Accepted");
                accidentLeaveForm.setHead(approvalDTO.getUser());
                accidentLeaveForm.setHeadDescription(approvalDTO.getDescription());
                accidentLeaveForm.setHeadReactedAt(new Date());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            } else if (job.equals("Dean")) {
                accidentLeaveForm.setDeanStatus("Accepted");
                accidentLeaveForm.setDean(approvalDTO.getUser());
                accidentLeaveForm.setDeanDescription(approvalDTO.getDescription());
                accidentLeaveForm.setDeanReactedAt(new Date());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }else if (job.equals("Chief Medical Officer")) {
                accidentLeaveForm.setCmoStatus("Accepted");
                accidentLeaveForm.setCmo(approvalDTO.getUser());
                accidentLeaveForm.setCmoDescription(approvalDTO.getDescription());
                accidentLeaveForm.setCmoReactedAt(new Date());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }else if (job.equals("Non Academic Establishment Division")) {
                accidentLeaveForm.setNaeStatus("Accepted");
                accidentLeaveForm.setNae(approvalDTO.getUser());
                accidentLeaveForm.setNaeDescription(approvalDTO.getDescription());
                accidentLeaveForm.setNaeReactedAt(new Date());
                accidentLeaveForm.setStatus("Accepted");
                mailService.sendMail(accidentLeaveForm.getUser().getEmail(), url, accidentLeaveForm.getUser().getFirst_name(), accidentLeaveForm.getFormType() , "Accepted", approver.getFirst_name());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        AccidentLeaveForm accidentLeaveForm = accidentLeaveFormRepo.findById(formId).orElse(null);
        if(accidentLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            if (job.equals("Head of the Department")) {
                accidentLeaveForm.setHeadStatus("Rejected");
                accidentLeaveForm.setDean(approvalDTO.getUser());
                accidentLeaveForm.setDeanDescription(approvalDTO.getDescription());
                accidentLeaveForm.setHeadReactedAt(new Date());
                accidentLeaveForm.setStatus("Rejected");
                mailService.sendMail(accidentLeaveForm.getUser().getEmail(), url, accidentLeaveForm.getUser().getFirst_name(), accidentLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }else if (job.equals("Dean")) {
                accidentLeaveForm.setDeanStatus("Rejected");
                accidentLeaveForm.setDean(approvalDTO.getUser());
                accidentLeaveForm.setDeanDescription(approvalDTO.getDescription());
                accidentLeaveForm.setDeanReactedAt(new Date());
                accidentLeaveForm.setStatus("Rejected");
                mailService.sendMail(accidentLeaveForm.getUser().getEmail(), url, accidentLeaveForm.getUser().getFirst_name(), accidentLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }else if (job.equals("Chief Medical Officer")) {
                accidentLeaveForm.setCmoStatus("Rejected");
                accidentLeaveForm.setCmo(approvalDTO.getUser());
                accidentLeaveForm.setCmoDescription(approvalDTO.getDescription());
                accidentLeaveForm.setCmoReactedAt(new Date());
                accidentLeaveForm.setStatus("Rejected");
                mailService.sendMail(accidentLeaveForm.getUser().getEmail(), url, accidentLeaveForm.getUser().getFirst_name(), accidentLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }else if (job.equals("Non Academic Establishment Division")) {
                accidentLeaveForm.setNaeStatus("Rejected");
                accidentLeaveForm.setNae(approvalDTO.getUser());
                accidentLeaveForm.setNaeDescription(approvalDTO.getDescription());
                accidentLeaveForm.setNaeReactedAt(new Date());
                accidentLeaveForm.setStatus("Rejected");
                mailService.sendMail(accidentLeaveForm.getUser().getEmail(), url, accidentLeaveForm.getUser().getFirst_name(), accidentLeaveForm.getFormType() , "Rejected", approver.getFirst_name());
                return accidentLeaveFormRepo.save(accidentLeaveForm);
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId) {
        if(!accidentLeaveFormRepo.existsByUserId(userId)){
            return "there is no form for this user";
        }
        accidentLeaveFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) throws FormUnderProcessException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        AccidentLeaveForm accidentLeaveForm = accidentLeaveFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (accidentLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), accidentLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "SUPER_ADMIN")){
            if(Objects.equals(accidentLeaveForm.getHeadStatus(), "pending")){
                accidentLeaveFormRepo.deleteById(id);
                return "Form deleted Successfully";
            }
            throw new FormUnderProcessException("Form is under process, Can't delete!!!");
        }
        return "Form deleted rejected";
    }

    public Collection<AccidentLeaveForm> getFormsOfUserById(String id) {
        return accidentLeaveFormRepo.findByUserId(id);
    }
}
