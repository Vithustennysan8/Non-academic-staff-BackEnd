package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.DynamicFormAlreadyExistsException;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import com.Non_academicWebsite.Entity.Forms.FormField;
import com.Non_academicWebsite.Entity.Forms.FormOption;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.DynamicFormRepo;
import com.Non_academicWebsite.Repository.Forms.FormFieldRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.apache.catalina.LifecycleState;
import org.hibernate.type.descriptor.java.IncomparableComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamicFormService {

    @Autowired
    private DynamicFormRepo dynamicFormRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private FormFieldRepo formFieldRepo;
    @Autowired
    private FormOptionService formOptionService;


    public DynamicForm createForm(String formType, String department, String faculty) throws DynamicFormAlreadyExistsException {
        if(dynamicFormRepo.existsByFormTypeAndDepartmentAndFaculty(formType, department, faculty)){
            throw new DynamicFormAlreadyExistsException("Dynamic form already exists!!!");
        }
        DynamicForm dynamicForm = DynamicForm.builder()
                .formType(formType)
                .department(department)
                .faculty(faculty)
                .build();
        dynamicFormRepo.save(dynamicForm);
        return dynamicForm;
    }

    public DynamicForm getForm(String formType, String department, String faculty) {
        if(!dynamicFormRepo.existsByFormTypeAndDepartmentAndFaculty(formType, department, faculty)){
            throw new NullPointerException("dynamic form not found");
        }
        return dynamicFormRepo.findByFormTypeAndDepartmentAndFaculty(formType, department, faculty);
    }

    public Object getDynamicForm(String header, String form) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(!dynamicFormRepo.existsByFormTypeAndDepartmentAndFaculty(form, user.getDepartment(), user.getFaculty())){
            return "There is no dynamic form";
        }
        DynamicForm dynamicForm = dynamicFormRepo.findByFormTypeAndDepartmentAndFaculty(form,
                user.getDepartment(), user.getFaculty());

        List<FormField> sortedFormFields = formFieldRepo.findAllByDynamicFormId(dynamicForm.getId()).stream()
                .sorted((f1, f2) -> IncomparableComparator.INSTANCE.compare(f1.getSequence(), f2.getSequence())
                ).toList();

        Map<String, Object> responseForm = new HashMap<>();
        responseForm.put("formType", dynamicForm.getFormType());
        responseForm.put("department", dynamicForm.getDepartment());
        responseForm.put("faculty", dynamicForm.getFaculty());

        List<Map<String, Object>> fields = new ArrayList<>();
        for(FormField field : sortedFormFields) {
            Map<String, Object> formFieldsMap = new HashMap<>();
            formFieldsMap.put("type", field.getFieldType());
            formFieldsMap.put("label", field.getFieldName());
            formFieldsMap.put("placeholder", field.getFieldPlaceholder());
            formFieldsMap.put("required", field.isRequired());
            formFieldsMap.put("sequence", field.getSequence());

            // Add options if field type is dropdown or radio button
             List<FormOption> options = formOptionService.findByFormFieldId(field.getId());
             List<String> values = new ArrayList<>();
             for(FormOption option : options) {
                 values.add(option.getValue());
             }
             formFieldsMap.put("options", values);
             fields.add(formFieldsMap);
        }
        responseForm.put("fields", fields);
        return responseForm;
    }

    public Object getAllDynamicForms(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        List<DynamicForm> dynamicForms = dynamicFormRepo.findAllByDepartmentAndFaculty(
                user.getDepartment(), user.getFaculty());

        List<String> responseForms = new ArrayList<>();
        for(DynamicForm dynamicForm : dynamicForms) {
            responseForms.add(dynamicForm.getFormType());
        }
        return responseForms;
    }
}
