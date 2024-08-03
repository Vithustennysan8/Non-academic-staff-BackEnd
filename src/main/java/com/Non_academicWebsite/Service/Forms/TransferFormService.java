package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.DTO.Forms.TransferFromDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Repository.Forms.TransferFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TransferFormService {
    @Autowired
    private TransferFormRepo transferFormRepo;

    public String submitForm(TransferFromDTO transferFromDTO, MultipartFile file) throws IOException {
        TransferForm form = TransferForm.builder()
                .name(transferFromDTO.getName())
                .emp_id(transferFromDTO.getEmp_id())
                .faculty(transferFromDTO.getFaculty())
                .department(transferFromDTO.getDepartment())
                .job_start_date(transferFromDTO.getJob_start_date())
                .experience(transferFromDTO.getExperience())
                .preference1(transferFromDTO.getPreference1())
                .preference2(transferFromDTO.getPreference2())
                .preference3(transferFromDTO.getPreference3())
                .reason(transferFromDTO.getReason())
                .file_data(file != null? file.getBytes(): null)
                .file_name(file != null? file.getOriginalFilename(): null)
                .file_type(file != null? file.getContentType(): null)
                .build();

        transferFormRepo.save(form);
        return "Submitted Successfully";
    }
}
