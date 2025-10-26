package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.Forms.FormFieldDTO;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import com.Non_academicWebsite.Entity.Forms.FormField;
import com.Non_academicWebsite.Entity.Forms.FormOption;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.FormFieldRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormFieldService {

    @Autowired
    private FormFieldRepo formFieldRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private DynamicFormService dynamicFormService;
    @Autowired
    private FormOptionService formOptionService;

    public Object createFormField(List<FormFieldDTO> formFieldDTOList, String header, String formType)
            throws ResourceExistsException, ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        // create the new dynamic form for the form fields
        // check if a dynamic form already exists for the form type and department and faculty
        DynamicForm form = dynamicFormService.createForm(formType, user.getDepartment(), user.getFaculty());

        int fieldOrder = 1;
        // save the form fields
        for (FormFieldDTO field : formFieldDTOList) {
            FormField formField = formFieldRepo.save(FormField.builder()
                            .dynamicForm(form)
                            .fieldName(field.getLabel())
                            .fieldType(field.getType())
                            .fieldPlaceholder(field.getPlaceholder())
                            .isRequired(field.isRequired())
                            .sequence(fieldOrder)
                            .build());

            if(!field.getOptions().isEmpty()) {
                field.getOptions().forEach(option -> {
                    FormOption formOption = FormOption.builder()
                            .formField(formField)
                            .value(option)
                            .build();
                    formOptionService.addOption(formOption);
                });
            }
            fieldOrder++;
        }
        return "Success";
    }

}
