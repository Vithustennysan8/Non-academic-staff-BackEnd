package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import com.Non_academicWebsite.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;

@RestController
@RequestMapping("api/auth")
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(value = "/signup")
    public ResponseEntity<?> register(@ModelAttribute RegisterDTO registerDTO,
                                      @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

        return ResponseEntity.ok(authenticationService.registerStaff(registerDTO, image));

    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO loginDTO) throws UserNotFoundException {
        return ResponseEntity
                .ok(authenticationService.login(loginDTO));
    }

}
