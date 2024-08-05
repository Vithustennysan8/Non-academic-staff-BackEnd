package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.DTO.SecurityDTO;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Response.UserInfoResponse;
import com.Non_academicWebsite.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/auth/user")
@CrossOrigin
public class UserController {
    @Autowired
    private StaffService staffService;

    @GetMapping(value = "/staffs")
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok( staffService.getUsers(header));
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserInfoResponse> getUser(@RequestHeader("Authorization") String header){
        String token = header.substring(7);
        return ResponseEntity.ok( staffService.getUser(token));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String header,
                              @RequestBody RegisterDTO registerDTO){
        staffService.updateProfile(header, registerDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/reset")
    public ResponseEntity<String> resetPassword(@RequestHeader("Authorization") String header,
                                @RequestBody SecurityDTO resetPasswordDTO){
        return ResponseEntity.ok(staffService.resetPassword(header, resetPasswordDTO));
    }

    @DeleteMapping( value = "/delete")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String header,
                              @RequestBody SecurityDTO deleteAccountDTO){
        return ResponseEntity.ok(staffService.deleteAccount(header,deleteAccountDTO));
    }

}
