package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.NormalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.Forms.NormalLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NormalLeaveFormService {

    private final NormalLeaveFormRepo normalLeaveFormRepo;
    private final UserRepo userRepo;
    private final MailService mailService;
    private final ExtractUserService extractUserService;
    @Value("${FrontEndURL}")
    private String url;


    public NormalLeaveForm add(String header, NormalLeaveFormDTO normalLeaveFormDTO)
            throws UnauthorizedAccessException, ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if (user.getRole() != Role.USER){
            throw new UnauthorizedAccessException("User only can request leaves");
        }

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
                .leaveAt(normalLeaveFormDTO.getLeaveAppliedDate())
                .leaveDays(normalLeaveFormDTO.getNoOfLeaveDays())
                .reason(normalLeaveFormDTO.getReason())
                .arrangement(normalLeaveFormDTO.getArrangement())
                .addressDuringTheLeave(normalLeaveFormDTO.getAddressDuringTheLeave())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .formType("Normal Leave Form")
                .headStatus("pending")
                .status("Pending")
                .build();

        return normalLeaveFormRepo.save(normalLeaveForm);
    }

    public Object getNormalLeaveForms(ReqFormsDTO reqFormsDTO, String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

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

    public List<NormalLeaveForm> getForms(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }
        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        return normalLeaveFormRepo.findByUserIdStartingWith(prefix);
    }

    public List<NormalLeaveForm> getFormsOfUser(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }
        return normalLeaveFormRepo.findByUserId(user.getId());
    }

    public Object acceptForm(Long formId, ApprovalDTO approvalDTO) {
        NormalLeaveForm normalLeaveForm = normalLeaveFormRepo.findById(formId).orElse(null);

        if(normalLeaveForm != null){
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            if (job.equals("Head of the Department")) {
                normalLeaveForm.setHeadStatus("Accepted");
                normalLeaveForm.setHead(approvalDTO.getUser());
                normalLeaveForm.setHeadDescription(approvalDTO.getDescription());
                normalLeaveForm.setHeadReactedAt(LocalDateTime.now());
                normalLeaveForm.setStatus("Accepted");
                mailService.sendMail(normalLeaveForm.getUser().getEmail(), url, normalLeaveForm.getUser().getFirst_name(), normalLeaveForm.getFormType() , "Accepted", approver.getFirst_name());
                return normalLeaveFormRepo.save(normalLeaveForm);
            }
        }
        return "Failed";
    }

    public Object rejectForm(Long formId, ApprovalDTO approvalDTO) {
        NormalLeaveForm normalLeaveForm = normalLeaveFormRepo.findById(formId).orElse(null);
        if(normalLeaveForm != null) {
            User user = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            User approver = userRepo.findById(approvalDTO.getUser()).orElseThrow();
            String job = user.getJobType();

            if (job.equals("Head of the Department")) {
                normalLeaveForm.setHeadStatus("Rejected");
                normalLeaveForm.setHead(approvalDTO.getUser());
                normalLeaveForm.setHeadDescription(approvalDTO.getDescription());
                normalLeaveForm.setHeadReactedAt(LocalDateTime.now());
                normalLeaveForm.setStatus("Rejected");
                mailService.sendMail(normalLeaveForm.getUser().getEmail(), url, normalLeaveForm.getUser().getFirst_name(), normalLeaveForm.getFormType(), "Rejected", approver.getFirst_name());
                return normalLeaveFormRepo.save(normalLeaveForm);
            }
        }
        return "Failed";
    }

    public String deleteForm(String userId) {
        if(!normalLeaveFormRepo.existsByUserId(userId)){
            return "There is no form found with this user id. Please check the user id again. Returning null...";
        }
        normalLeaveFormRepo.deleteByUserId(userId);
        return "delete success";
    }

    public String deleteByUser(Long id, String header) throws FormUnderProcessException, ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        NormalLeaveForm normalLeaveForm = normalLeaveFormRepo.findById(id).orElse(null);

        if(user == null) {
            throw new NullPointerException("User not found");
        } else if (normalLeaveForm == null) {
            throw new NullPointerException("Form not found");
        }else if (Objects.equals(user.getId(), normalLeaveForm.getUser().getId()) || Objects.equals(user.getRole().toString(), "SUPER_ADMIN")){
            if(Objects.equals(normalLeaveForm.getHeadStatus(), "pending")){
                normalLeaveFormRepo.deleteById(id);
                return "Form deleted Successfully";
            }
            throw new FormUnderProcessException("Form is under process, Can't delete!!!");
        }
        return "Form deleted rejected";
    }

    public Collection<NormalLeaveForm> getFormsOfUserById(String id) {
        return normalLeaveFormRepo.findByUserId(id);
    }

    public List<NormalLeaveForm> getFormsPending(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }
        return normalLeaveFormRepo.findByUserIdAndStatus(user.getId(), "pending");
    }
}
