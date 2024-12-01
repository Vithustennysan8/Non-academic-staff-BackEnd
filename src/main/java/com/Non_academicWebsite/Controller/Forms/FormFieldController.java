package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.DynamicFormAlreadyExistsException;
import com.Non_academicWebsite.DTO.Forms.FormFieldDTO;
import com.Non_academicWebsite.Service.Forms.FormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin/formField")
public class FormFieldController {

    @Autowired
    private FormFieldService formFieldService;

    @PostMapping(value = "/create/{formType}")
    public ResponseEntity<?> createForm(@RequestBody List<FormFieldDTO> formFieldDTOList,
                                        @RequestHeader("Authorization") String header,
                                        @PathVariable("formType") String formType)
                                        throws DynamicFormAlreadyExistsException {
        return ResponseEntity.ok(formFieldService.createFormField(formFieldDTOList, header, formType));
    }
}
