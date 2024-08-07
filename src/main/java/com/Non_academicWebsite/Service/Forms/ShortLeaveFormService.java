package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.Forms.ShortLeaveFormDTO;
import com.Non_academicWebsite.Entity.Forms.ShortLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.ShortLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ShortLeaveFormService {
    @Autowired
    private ShortLeaveFormRepo shortLeaveFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    public String submitForm(ShortLeaveFormDTO shortLeaveFormDTO, MultipartFile file, String header) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();

        ShortLeaveForm form = ShortLeaveForm.builder()
                .name(shortLeaveFormDTO.getName())
                .emp_id(shortLeaveFormDTO.getEmpId())
                .userId(user.getId())
                .faculty(shortLeaveFormDTO.getFaculty())
                .department(shortLeaveFormDTO.getDepartment())
                .job_start_date(shortLeaveFormDTO.getJob_start_date())
                .duration(shortLeaveFormDTO.getDuration())
                .leave_type(shortLeaveFormDTO.getLeave_type())
                .leave_date(shortLeaveFormDTO.getLeave_date())
                .reason(shortLeaveFormDTO.getReason())
                .file_data(file != null ? file.getBytes() : null)
                .file_name(file != null ? file.getOriginalFilename() : null)
                .file_type(file != null ? file.getContentType() : null)
                .status(true)
                .build();

        shortLeaveFormRepo.save(form);
        return "Submitted Successfully";
    }

    public List<ShortLeaveForm> getForms(String prefix) {
        List<ShortLeaveForm> forms = shortLeaveFormRepo.findByUserIdStartingWith(prefix);
        if (!forms.isEmpty()) {
            return forms;
        }
        return Collections.emptyList();
    }
}
