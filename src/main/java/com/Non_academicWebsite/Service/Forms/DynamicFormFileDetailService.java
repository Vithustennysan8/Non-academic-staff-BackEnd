package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Repository.Forms.DynamicFormFileDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynamicFormFileDetailService {

    private final DynamicFormFileDetailRepo dynamicFormFileDetailRepo;
}
