package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.Forms.SubtituteFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.SubtituteForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.SubtituteFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubtituteFormService {
    @Autowired
    private SubtituteFormRepo subtituteFormRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;


    public String submitForm(SubtituteFormDTO subtituteFormDTO, String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();

        SubtituteForm form = SubtituteForm.builder()
                .name(subtituteFormDTO.getName())
                .emp_id(subtituteFormDTO.getEmpId())
                .userId(user.getId())
                .faculty(subtituteFormDTO.getFaculty())
                .department(subtituteFormDTO.getDepartment())
                .start_date(subtituteFormDTO.getStart_date())
                .end_date(subtituteFormDTO.getEnd_date())
                .acting(subtituteFormDTO.getActing())
                .reason(subtituteFormDTO.getReason())
                .status(true)
                .build();

        subtituteFormRepo.save(form);
        return "Submitted Successfully";
    }

    public List<SubtituteForm> getForms(String prefix) {
        List<SubtituteForm> forms = subtituteFormRepo.findByUserIdStartingWith(prefix);
        if (!forms.isEmpty()) {
            return forms;
        }
        return Collections.emptyList();
    }

    public List<SubtituteForm> getSubtituteForms(ReqFormsDTO reqFormsDTO) {
        if(reqFormsDTO.getDepartment().isEmpty()){
            return subtituteFormRepo.findByFaculty(reqFormsDTO.getFaculty());
        }
        return subtituteFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }
}
