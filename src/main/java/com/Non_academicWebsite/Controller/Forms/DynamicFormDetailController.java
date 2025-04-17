package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.DynamicFormNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Service.Forms.DynamicFormDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/auth/user/dynamicFormDetail")
public class DynamicFormDetailController {

    @Autowired
    private DynamicFormDetailService dynamicFormDetailService;

    @PostMapping("/add/{form}/{flow}")
    public ResponseEntity<?> addDynamicFormDetails(@RequestHeader("Authorization") String header,
                                                   @PathVariable("flow") String flow,
                                                   @PathVariable("form") String form,
                                                   @RequestParam Map<String, String> parameters,
                                                   @RequestParam(value = "file", required = false) List<MultipartFile> files) throws DynamicFormNotFoundException, UnauthorizedAccessException {
        return ResponseEntity.ok(dynamicFormDetailService.addDynamicFormDetails(header, parameters, files, form, flow));
    }
};
