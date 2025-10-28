package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.PartialFileUploadException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Service.Forms.DynamicFormDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/user/dynamicFormDetail")
@RequiredArgsConstructor
public class DynamicFormDetailController {

    private final DynamicFormDetailService dynamicFormDetailService;

    @PostMapping("/add/{form}/{flow}")
    public ResponseEntity<?> addDynamicFormDetails(@RequestHeader("Authorization") String header,
                                                   @PathVariable("flow") String flow,
                                                   @PathVariable("form") String form,
                                                   @RequestParam Map<String, String> parameters,
                                                   @RequestParam(value = "file", required = false) List<MultipartFile> files)
            throws UnauthorizedAccessException, ResourceNotFoundException, PartialFileUploadException {
        return ResponseEntity.status(HttpStatus.CREATED).body(dynamicFormDetailService
                .addDynamicFormDetails(header, parameters, files, form, flow));
    }
};
