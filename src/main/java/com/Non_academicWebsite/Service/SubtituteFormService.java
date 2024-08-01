package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.SubtituteFormDTO;
import com.Non_academicWebsite.Entity.SubtituteForm;
import com.Non_academicWebsite.Repository.SubtituteFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubtituteFormService {
    @Autowired
    private SubtituteFormRepo subtituteFormRepo;

    public String submitForm(SubtituteFormDTO subtituteFormDTO) {
        SubtituteForm form = SubtituteForm.builder()
                .name(subtituteFormDTO.getName())
                .emp_id(subtituteFormDTO.getEmp_id())
                .faculty(subtituteFormDTO.getFaculty())
                .department(subtituteFormDTO.getDepartment())
                .start_date(subtituteFormDTO.getStart_date())
                .end_date(subtituteFormDTO.getEnd_date())
                .acting(subtituteFormDTO.getActing())
                .reason(subtituteFormDTO.getReason())
                .build();

        subtituteFormRepo.save(form);
        return "Submitted Successfully";
    }
}
