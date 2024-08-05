package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.Forms.FullLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.FullLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.FullLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class FullLeaveFormService {
    @Autowired
    private FullLeaveFormRepo fullLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public String submitForm(FullLeaveFormDTO fullLeaveFormDTO, MultipartFile file, String header) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();

        FullLeaveForm form = FullLeaveForm.builder()
                .name(fullLeaveFormDTO.getName())
                .empId(fullLeaveFormDTO.getEmpId())
                .userId(user.getId())
                .faculty(fullLeaveFormDTO.getFaculty())
                .department(fullLeaveFormDTO.getDepartment())
                .job_start_date(fullLeaveFormDTO.getJob_start_date())
                .leave_days(fullLeaveFormDTO.getLeave_days())
                .leave_type(fullLeaveFormDTO.getLeave_type())
                .start_date(fullLeaveFormDTO.getStart_date())
                .end_date(fullLeaveFormDTO.getEnd_date())
                .acting(fullLeaveFormDTO.getActing())
                .reason(fullLeaveFormDTO.getReason())
                .file_data(file != null? file.getBytes(): null)
                .file_name(file != null? file.getOriginalFilename(): null)
                .file_type(file != null? file.getContentType(): null)
                .status(true)
                .build();

        fullLeaveFormRepo.save(form);
        return "Submitted Successfully";
    }

    public List<FullLeaveForm> getForms(String prefix) {
            List<FullLeaveForm> forms = fullLeaveFormRepo.findByUserIdStartingWith(prefix);
            if (!forms.isEmpty()){
                return forms;
            }
        return Collections.emptyList();
    }
}
