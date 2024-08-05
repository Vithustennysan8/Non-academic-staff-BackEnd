package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.Forms.TransferFromDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.TransferFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class TransferFormService {
    @Autowired
    private TransferFormRepo transferFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;


    public String submitForm(TransferFromDTO transferFromDTO, MultipartFile file, String header) throws IOException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();

        TransferForm form = TransferForm.builder()
                .name(transferFromDTO.getName())
                .emp_id(transferFromDTO.getEmpId())
                .userId(user.getId())
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
                .status(true)
                .build();

        transferFormRepo.save(form);
        return "Submitted Successfully";
    }

    public List<TransferForm> getForms(String prefix) {
        List<TransferForm> forms = transferFormRepo.findByUserIdStartingWith(prefix);
        if (!forms.isEmpty()){
            return forms;
        }
        return Collections.emptyList();
    }
}
