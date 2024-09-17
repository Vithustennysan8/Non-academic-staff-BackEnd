package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.NormalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.NormalLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class NormalLeaveFormService {
    @Autowired
    private NormalLeaveFormRepo normalLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public NormalLeaveForm add(String header, NormalLeaveFormDTO normalLeaveFormDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .upfNo(normalLeaveFormDTO.getUpfNo())
                .designation(normalLeaveFormDTO.getDesignation())
                .firstAppointmentDate(normalLeaveFormDTO.getFirstAppointmentDate())
                .casualLeaveLastYear(normalLeaveFormDTO.getCasualLeaveLastYear())
                .sickLeaveLastYear(normalLeaveFormDTO.getSickLeaveLastYear())
                .vacationLeaveLastYear(normalLeaveFormDTO.getVacationLeaveLastYear())
                .casualLeaveThisYear(normalLeaveFormDTO.getCasualLeaveThisYear())
                .sickLeaveThisYear(normalLeaveFormDTO.getSickLeaveThisYear())
                .vacationLeaveThisYear(normalLeaveFormDTO.getVacationLeaveThisYear())
                .addressDuringTheLeave(normalLeaveFormDTO.getAddressDuringTheLeave())
                .noOfLeaveDays(normalLeaveFormDTO.getNoOfLeaveDays())
                .leaveType(normalLeaveFormDTO.getLeaveType())
                .leaveAppliedDate(normalLeaveFormDTO.getLeaveAppliedDate())
                .reason(normalLeaveFormDTO.getReason())
                .arrangement(normalLeaveFormDTO.getArrangement())
                .addressDuringTheLeave(normalLeaveFormDTO.getAddressDuringTheLeave())
                .orderOfHead(normalLeaveFormDTO.getOrderOfHead())
                .user(user)
                .formType("Normal Leave Form")
                .approverOneStatus("pending")
                .status("Pending")
                .build();

        return normalLeaveFormRepo.save(normalLeaveForm);
    }

    public Object getNormalLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        if(reqFormsDTO.getDepartment() == null && reqFormsDTO.getFaculty() == null){
            return normalLeaveFormRepo.findByUserIdStartingWith(prefix);
        }
        if(reqFormsDTO.getFaculty() == null){
            return normalLeaveFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
        }
        if(reqFormsDTO.getDepartment() == null){
            return normalLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return normalLeaveFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    public List<NormalLeaveForm> getForms(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return normalLeaveFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<NormalLeaveForm> getFormsOfUser(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }
        return normalLeaveFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Integer formId, ApprovalDTO approvalDTO) {
        NormalLeaveForm normalLeaveForm = normalLeaveFormRepo.findById(formId).orElse(null);
        if(normalLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                normalLeaveForm.setApproverOneStatus("Accepted");
                normalLeaveForm.setApproverOne(approvalDTO.getUser());
                normalLeaveForm.setApproverOneDescription(approvalDTO.getDescription());
                normalLeaveForm.setApproverOneReactedAt(new Date());
                normalLeaveForm.setStatus("Accepted");
            }
            return normalLeaveFormRepo.save(normalLeaveForm);
        }
        return "Failed";
    }

    public Object rejectForm(Integer formId, ApprovalDTO approvalDTO) {
        NormalLeaveForm normalLeaveForm = normalLeaveFormRepo.findById(formId).orElse(null);
        if(normalLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                normalLeaveForm.setApproverOneStatus("Rejected");
                normalLeaveForm.setApproverOne(approvalDTO.getUser());
                normalLeaveForm.setApproverOneDescription(approvalDTO.getDescription());
                normalLeaveForm.setApproverOneReactedAt(new Date());
                normalLeaveForm.setStatus("Rejected");
            }
            return normalLeaveFormRepo.save(normalLeaveForm);
        }
        return "Failed";
    }

}
