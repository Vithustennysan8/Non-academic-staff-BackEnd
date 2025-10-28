package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Entity.Forms.FormOption;
import com.Non_academicWebsite.Repository.Forms.FormOptionsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormOptionService {

    private final FormOptionsRepo formOptionsRepo;


    public void addOption(FormOption formOption) {
        formOptionsRepo.save(formOption);
    }

    public List<FormOption> findByFormFieldId(Long id) {
        return formOptionsRepo.findAllByFormFieldId(id);
    }
}
