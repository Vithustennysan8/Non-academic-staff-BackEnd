package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.DTO.ForgotPasswordDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.DTO.SecurityDTO;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.NormalLeaveFormRepo;
import com.Non_academicWebsite.Response.UserInfoResponse;
import com.Non_academicWebsite.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class UserController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private NormalLeaveFormRepo normalLeaveFormRepo;

    @GetMapping("/auth/user/get")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("USER::get");
    }

    @GetMapping(value = "/auth/user/staffs")
    public ResponseEntity<List<User>> getUsersByDepartment(@RequestHeader("Authorization") String header)
            throws UserNotFoundException {
        return ResponseEntity.ok(staffService.getUsersByDepartment(header));
    }

    @GetMapping(value = "/auth/user/info")
    public ResponseEntity<UserInfoResponse> getUser(@RequestHeader("Authorization") String header)
                                                    throws UserNotFoundException {
        String token = header.substring(7);
        return ResponseEntity.ok(staffService.getUser(token));
    }

    @PutMapping(value = "/auth/user/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String header,
                                           @ModelAttribute RegisterDTO registerDTO,
                                           @RequestParam(value = "image", required = false) MultipartFile image)
                                            throws IOException, UserNotFoundException {
        User user = staffService.updateProfile(header, registerDTO, image);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/auth/user/reset")
    public ResponseEntity<String> resetPassword(@RequestHeader("Authorization") String header,
                                                @RequestBody SecurityDTO resetPasswordDTO) throws UserNotFoundException {
        return ResponseEntity.ok(staffService.resetPassword(header, resetPasswordDTO));
    }

    @DeleteMapping(value = "/auth/user/delete")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String header,
                                                @RequestBody SecurityDTO deleteAccountDTO)
                                                throws UserNotFoundException {
        return ResponseEntity.ok(staffService.deleteAccount(header, deleteAccountDTO));
    }

    @GetMapping("/auth/user/leaveForms")
    public ResponseEntity<?> getAllAppliedLeaveForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(staffService.getAllAppliedLeaveForms(header));
    }

    @GetMapping("/auth/user/leaveFormsById/{id}")
    public ResponseEntity<?> getAllAppliedLeaveFormsById(@PathVariable("id") String id) throws UserNotFoundException {
        return ResponseEntity.ok(staffService.getAllAppliedLeaveFormsById(id));
    }

    @GetMapping("/auth/user/transferForms")
    public ResponseEntity<?> getAllTransferForms(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(staffService.getAllTransferForms(header));
    }

    @PostMapping("/auth/user/forgotPassword/sendOTP")
    public ResponseEntity<String> sendOTP(@RequestBody ForgotPasswordDTO forgotPasswordDTO) throws UserNotFoundException {
        return ResponseEntity.ok(staffService.sendOTP(forgotPasswordDTO.getEmail()));
    }

    @PostMapping("/auth/user/forgotPassword/confirmation")
    public ResponseEntity<String> confirmOTP(@RequestBody ForgotPasswordDTO forgotPasswordDTO)
                                            throws UserNotFoundException {
        return ResponseEntity.ok(staffService.confirmOTP(forgotPasswordDTO.getOtp()));
    }

    @PostMapping("/auth/user/forgotPassword/reset")
    public ResponseEntity<String> resetForForgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO)
                                                        throws UserNotFoundException {
        return ResponseEntity.ok(staffService.resetForForgotPassword(forgotPasswordDTO));
    }

    @GetMapping("admin/managers")
    public ResponseEntity<List<User>> getAllManagers(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(staffService.getAllManagers(header));
    }

    @PutMapping("admin/incharge/{id}")
    public ResponseEntity<List<User>> assignAsAdmin(@PathVariable("id") String id, @RequestHeader("Authorization") String header) throws UserNotFoundException {
        return ResponseEntity.ok(staffService.assignAsAdmin(id, header));
    }

    @PutMapping("admin/unIncharge/{id}")
    public ResponseEntity<List<User>> unAssignAsAdmin(@PathVariable("id") String id, @RequestHeader("Authorization") String header) throws UserNotFoundException, UnauthorizedAccessException {
        return ResponseEntity.ok(staffService.unAssignAsAdmin(id, header));
    }

}
