package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.Service.Forms.DynamicFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/user/dynamicForm")
public class DynamicFormController {

    @Autowired
    private DynamicFormService dynamicFormService;

    @GetMapping(value = "/get/{form}")
    public ResponseEntity<?> getDynamicForm(@RequestHeader("Authorization") String header,
                                            @PathVariable("form") String form){
        return ResponseEntity.ok(dynamicFormService.getDynamicForm(header, form));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllDynamicForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(dynamicFormService.getAllDynamicForms(header));
    }
}
