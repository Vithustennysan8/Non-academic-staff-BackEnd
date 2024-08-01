package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.ShortLeaveFormDTO;
import com.Non_academicWebsite.Entity.FullLeaveForm;
import com.Non_academicWebsite.Entity.ShortLeaveForm;
import com.Non_academicWebsite.Repository.ShortLeaveFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ShortLeaveFormService {
    @Autowired
    private ShortLeaveFormRepo shortLeaveFormRepo;

    public String submitForm(ShortLeaveFormDTO shortLeaveFormDTO, MultipartFile file) throws IOException {
        ShortLeaveForm form = ShortLeaveForm.builder()
                .name(shortLeaveFormDTO.getName())
                .emp_id(shortLeaveFormDTO.getEmp_id())
                .faculty(shortLeaveFormDTO.getFaculty())
                .department(shortLeaveFormDTO.getDepartment())
                .job_start_date(shortLeaveFormDTO.getJob_start_date())
                .duration(shortLeaveFormDTO.getDuration())
                .leave_type(shortLeaveFormDTO.getLeave_type())
                .leave_date(shortLeaveFormDTO.getLeave_date())
                .reason(shortLeaveFormDTO.getReason())
                .file_data(file.getBytes())
                .file_name(file.getOriginalFilename())
                .file_type(file.getContentType())
                .build();

        shortLeaveFormRepo.save(form);
        return "Submitted Successfully";
    }
}
