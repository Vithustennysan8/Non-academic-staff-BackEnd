package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.DynamicFormAlreadyExistsException;
import com.Non_academicWebsite.DTO.Forms.FormFieldDTO;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import com.Non_academicWebsite.Service.Forms.DynamicFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/")
public class DynamicFormController {

    @Autowired
    private DynamicFormService dynamicFormService;


    @PostMapping(value = "/admin/dynamicForm/create/{formType}")
    public ResponseEntity<?> createForm(@RequestBody List<FormFieldDTO> formFieldDTOList,
                                        @RequestHeader("Authorization") String header,
                                        @PathVariable("formType") String formType)
                                        throws DynamicFormAlreadyExistsException {
        return ResponseEntity.ok(dynamicFormService.createFormField(formFieldDTOList, header, formType));
    }

    @GetMapping(value = "/auth/user/dynamicForm/get/{form}")
    public ResponseEntity<?> getDynamicForm(@RequestHeader("Authorization") String header,
                                            @PathVariable("form") String form){
        return ResponseEntity.ok(dynamicFormService.getDynamicForm(header, form));
    }

    @GetMapping(value = "/admin/dynamicForm/getAll")
    public ResponseEntity<?> getAllDynamicForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(dynamicFormService.getAllDynamicForms(header));
    }

    @GetMapping(value = "/auth/user/dynamicForm/getAll")
    public ResponseEntity<?> getAllDynamicFormsForUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(dynamicFormService.getAllDynamicFormsForUser(header));
    }

    @DeleteMapping(value = "/auth/user/dynamicForm/delete/{id}")
    public ResponseEntity<List<DynamicForm>> deleteForm(@PathVariable("id") Long id,
                                                        @RequestHeader("Authorization") String header) {

        return ResponseEntity.ok(dynamicFormService.deleteForm(id, header));
    }
}
