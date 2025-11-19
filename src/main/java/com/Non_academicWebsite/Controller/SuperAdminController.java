package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/superadmin")
@CrossOrigin
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    // used to retrieve the admin and user register requests for super_admin to approve
    @GetMapping(value = "/verifyAdminRegisterRequests")
    public ResponseEntity<List<RegisterConfirmationToken>> getVerifyAdminRegisterRequests(@RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(superAdminService.getVerifyAdminRegisterRequests(header));
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id,
                                            @RequestHeader("Authorization") String header)
            throws ResourceNotFoundException, UnauthorizedAccessException {
        return ResponseEntity.ok(superAdminService.deleteUser(id, header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteRegisterUser(@PathVariable("id") String id,
                                            @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(superAdminService.deleteRegisterUser(id, header));
    }

}
