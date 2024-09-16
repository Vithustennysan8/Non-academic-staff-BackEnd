package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.AccidentLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AccidentLeaveFormService {
    @Autowired
    private AccidentLeaveFormRepo accidentLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public AccidentLeaveForm add(String header, AccidentLeaveFormDTO accidentLeaveFormDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .accidentOccurredDuring(accidentLeaveFormDTO.getAccidentOccurredDuring())
                .DateAndTimeOfAccident(accidentLeaveFormDTO.getDateAndTimeOfAccident())
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
                .user(user)
                .formType("Accident Leave Form")
                .approverOneStatus("pending")
                .status("pending")
                .createdAt(new Date())
                .build();

        return accidentLeaveFormRepo.save(accidentLeaveForm);
    }

    public Object getAccidentLeaveForms(ReqFormsDTO reqFormsDTO, String header) {
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

    public Object acceptForm(Integer formId, ApprovalDTO approvalDTO) {
        AccidentLeaveForm accidentLeaveForm = accidentLeaveFormRepo.findById(formId).orElse(null);
        if(accidentLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                accidentLeaveForm.setApproverOneStatus("Accepted");
                accidentLeaveForm.setApproverOne(approvalDTO.getUser());
                accidentLeaveForm.setApproverOneDescription(approvalDTO.getDescription());
                accidentLeaveForm.setApproverOneReactedAt(new Date());
            } else if (job.equals("Manager")) {
                accidentLeaveForm.setApproverTwoStatus("Accepted");
                accidentLeaveForm.setApproverTwo(approvalDTO.getUser());
                accidentLeaveForm.setApproverTwoDescription(approvalDTO.getDescription());
                accidentLeaveForm.setApproverTwoReactedAt(new Date());
                accidentLeaveForm.setStatus("accepted");
            }
            return accidentLeaveFormRepo.save(accidentLeaveForm);
        }
        return "Failed";
    }

    public Object rejectForm(Integer formId, ApprovalDTO approvalDTO) {
        AccidentLeaveForm accidentLeaveForm = accidentLeaveFormRepo.findById(formId).orElse(null);
        if(accidentLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJob_type();

            if (job.equals("Head of the Department")) {
                accidentLeaveForm.setApproverOneStatus("Rejected");
                accidentLeaveForm.setApproverOne(approvalDTO.getUser());
                accidentLeaveForm.setApproverOneDescription(approvalDTO.getDescription());
                accidentLeaveForm.setApproverOneReactedAt(new Date());
                accidentLeaveForm.setStatus("rejected");
            }else if (job.equals("Manager")) {
                accidentLeaveForm.setApproverTwoStatus("Rejected");
                accidentLeaveForm.setApproverTwo(approvalDTO.getUser());
                accidentLeaveForm.setApproverTwoDescription(approvalDTO.getDescription());
                accidentLeaveForm.setApproverTwoReactedAt(new Date());
                accidentLeaveForm.setStatus("rejected");
            }
            return accidentLeaveFormRepo.save(accidentLeaveForm);
        }
        return "Failed";
    }
}
