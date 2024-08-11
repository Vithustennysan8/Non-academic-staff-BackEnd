package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.FullLeaveForm;
import com.Non_academicWebsite.Repository.Forms.FullLeaveFormRepo;
import com.Non_academicWebsite.Repository.Forms.ShortLeaveFormRepo;
import com.Non_academicWebsite.Repository.Forms.SubtituteFormRepo;
import com.Non_academicWebsite.Repository.Forms.TransferFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private FullLeaveFormRepo fullLeaveFormRepo;
    @Autowired
    private ShortLeaveFormRepo shortLeaveFormRepo;
    @Autowired
    private SubtituteFormRepo subtituteFormRepo;
    @Autowired
    private TransferFormRepo transferFormRepo;

    public List<?> getAllReqForms(ReqFormsDTO reqFormsDTO) {
        List<FullLeaveForm> forms = new ArrayList<>();
        forms.addAll(fullLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty()));
        return forms;
    }
}
