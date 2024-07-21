package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import com.Non_academicWebsite.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(value = "/signup")
    public ResponseEntity<Boolean> register(@RequestBody RegisterDTO registerDTO) throws Exception {
        return ResponseEntity.ok(authenticationService.registerStaff(registerDTO));

    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity
                .ok(authenticationService.login(loginDTO));
    }

}
