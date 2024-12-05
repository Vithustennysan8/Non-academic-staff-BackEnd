package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.DynamicFormDetailRepo;
import com.Non_academicWebsite.Repository.Forms.DynamicFormUserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DynamicFormUserService {

    @Autowired
    private DynamicFormUserRepo dynamicFormUserRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private DynamicFormDetailRepo dynamicFormDetailRepo;

    public Object getAllFormApplied(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<DynamicFormUser> userAppliedList = dynamicFormUserRepo.findByUserId(user.getId());
        if (userAppliedList == null) {return Collections.emptyList();};

        Map<String, Object> allForms = generateOutput(userAppliedList);
        return allForms.values().toArray();
    }

    public Object getAllFormRequests(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);

        List<DynamicFormUser> userAppliedList = dynamicFormUserRepo.findByUserIdStartingWith(prefix);
        if (userAppliedList == null) {return Collections.emptyList();};

        Map<String, Object> allForms = generateOutput(userAppliedList);
        return allForms.values().toArray();
    }

    public Map<String, Object> generateOutput(List<DynamicFormUser> list) {
        Map<String, Object> allForms = new HashMap<>();
        for(DynamicFormUser dynamicFormUser : list){
            List<DynamicFormDetail> details = dynamicFormDetailRepo.findByDynamicFormUserId(dynamicFormUser.getId());
            Map<String, Object> form =  new HashMap<>();
            form.put("form", dynamicFormUser.getDynamicForm().getFormType());
            form.put("userName", dynamicFormUser.getUser().getFirst_name());
            form.put("formStatus", dynamicFormUser.getStatus());
            form.put("formCreatedAt", dynamicFormUser.getCreatedAt());
            form.put("formDetails", new ArrayList<Map<String,String>>());

            List<Map<String, String>> detail = (List<Map<String, String>>) form.get("formDetails");
            details.forEach(dynamicFormDetail -> {
                detail.add(Map.of(dynamicFormDetail.getTag(), dynamicFormDetail.getValue()));
            });
            allForms.put(dynamicFormUser.getCreatedAt().toString(), form);
        }
        return allForms;
    }
}
