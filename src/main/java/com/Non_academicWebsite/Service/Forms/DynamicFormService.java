package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.Forms.FormFieldDTO;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import com.Non_academicWebsite.Entity.Forms.FormField;
import com.Non_academicWebsite.Entity.Forms.FormOption;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.DynamicFormRepo;
import com.Non_academicWebsite.Repository.Forms.FormFieldRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.IncomparableComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DynamicFormService {

    private final DynamicFormRepo dynamicFormRepo;
    private final ExtractUserService extractUserService;
    private final FormFieldRepo formFieldRepo;
    private final FormOptionService formOptionService;
    private final UserRepo userRepo;


    public Object createFormField(List<FormFieldDTO> formFieldDTOList, String header, String formType)
            throws ResourceNotFoundException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        // create the new dynamic form for the form fields
        // check if a dynamic form already exists for the form type and department and faculty
        DynamicForm form = createForm(formType, user.getDepartment(), user.getFaculty());

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

    public DynamicForm createForm(String formType, String department, String faculty)
            throws ResourceExistsException {

        if(dynamicFormRepo.existsByFormTypeAndDepartmentAndFacultyAndIsAvailable(formType, department,
                faculty, true)){
            throw new ResourceExistsException("Dynamic form already exists!!!");
        }
        DynamicForm dynamicForm = DynamicForm.builder()
                .formType(formType)
                .department(department)
                .faculty(faculty)
                .isAvailable(true)
                .build();
        dynamicFormRepo.save(dynamicForm);
        return dynamicForm;
    }

    public DynamicForm getForm(String formType, String department, String faculty) throws ResourceNotFoundException {
        if(!dynamicFormRepo.existsByFormTypeAndDepartmentAndFacultyAndIsAvailable(formType,
                department, faculty, true)){
            throw new ResourceNotFoundException("dynamic form not found");
        }
        return dynamicFormRepo.findByFormTypeAndDepartmentAndFacultyAndIsAvailable(formType,
                department, faculty, true);
    }

    public Object getDynamicForm(String header, String form) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(!dynamicFormRepo.existsByFormTypeAndDepartmentAndFacultyAndIsAvailable(form, user.getDepartment(),
                user.getFaculty(), true)){
            throw new ResourceNotFoundException("There is no dynamic form");
        }
        DynamicForm dynamicForm = dynamicFormRepo.findByFormTypeAndDepartmentAndFacultyAndIsAvailable(form,
                user.getDepartment(), user.getFaculty(), true);

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

    public Object getAllDynamicForms(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        return dynamicFormRepo.findAllByDepartmentAndFaculty(user.getDepartment(),
                user.getFaculty());
    }

    public List<DynamicForm> deleteForm(Long id, String header) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        DynamicForm dynamicForm = dynamicFormRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Form not found"));

        if( user.getRole() != Role.USER || (!dynamicForm.getDepartment().equals(user.getDepartment())
                ||!dynamicForm.getFaculty().equals(user.getFaculty()))) {
            throw new UnauthorizedAccessException( "You do not have permission to delete this dynamic form");
        }

        // make sure it is not available it seems to be unavailable/ deleted
        dynamicForm.setAvailable(!dynamicForm.isAvailable());
        dynamicFormRepo.save(dynamicForm);

        return dynamicFormRepo.findAllByDepartmentAndFaculty(user.getDepartment(), user.getFaculty());
    }

    public Object getAllDynamicFormsForUser(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        return dynamicFormRepo.findAllByDepartmentAndFacultyAndIsAvailable(user.getDepartment(),
                user.getFaculty(), true);
    }

    public Object getAllDynamicFormsForUserById(String id) throws ResourceNotFoundException {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " does not exist"));

        List<DynamicForm> forms =  dynamicFormRepo.findAllByDepartmentAndFacultyAndIsAvailable(user.getDepartment(),
                user.getFaculty(), true);

        Map<String, Integer> dynamicFormsAndCount = new HashMap<>();
        for(DynamicForm form : forms) {
            dynamicFormsAndCount.put(form.getFormType(), 0);
        }
        return dynamicFormsAndCount;
    }

    public Object getAllDynamicFormsByFacultyAndDepartment(String faculty, String department) {
        List<DynamicForm> forms =  dynamicFormRepo.findAllByDepartmentAndFacultyAndIsAvailable(department,
                faculty, true);

        Map<String, Integer> dynamicFormsAndCount = new HashMap<>();
        for(DynamicForm form : forms) {
            dynamicFormsAndCount.put(form.getFormType(), 0);
        }
        return dynamicFormsAndCount;
    }

    public Object getAllDynamicFormsForApprover(String header, String department, String faculty) {
        return dynamicFormRepo.findAllByDepartmentAndFaculty(department, faculty);
    }
}
