package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Repository.Forms.DynamicFormFileDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicFormFileDetailService {

    @Autowired
    private DynamicFormFileDetailRepo dynamicFormFileDetailRepo;
}
