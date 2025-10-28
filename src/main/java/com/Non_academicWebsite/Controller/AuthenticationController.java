package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import com.Non_academicWebsite.Service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterDTO registerDTO,
                                      @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        System.out.println(registerDTO.toString());
        return ResponseEntity.ok(authenticationService.registerStaff(registerDTO, image));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO loginDTO) throws
            UnauthorizedAccessException, ResourceNotFoundException {
        return ResponseEntity
                .ok(authenticationService.login(loginDTO));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody Map<String, String> token) throws ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.refresh(token));
    }

}
