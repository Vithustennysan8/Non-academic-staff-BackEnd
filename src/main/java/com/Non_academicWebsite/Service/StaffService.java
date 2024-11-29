package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.DTO.ForgotPasswordDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.DTO.SecurityDTO;
import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
import com.Non_academicWebsite.Entity.Forms.PaternalLeaveForm;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
//import com.Non_academicWebsite.Repository.AttendanceRepo;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Response.UserInfoResponse;
import com.Non_academicWebsite.Service.Forms.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class StaffService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegisterConfirmationTokenRepo registerConfirmationTokenRepo;
    @Autowired
    private ForumRepo forumRepo;
    @Autowired
    private NormalLeaveFormService normalLeaveFormService;
    @Autowired
    private AccidentLeaveFormService accidentLeaveFormService;
    @Autowired
    private PaternalLeaveFormService paternalLeaveFormService;
    @Autowired
    private MaternityLeaveFormService maternityLeaveFormService;
    @Autowired
    private MedicalLeaveFormService medicalLeaveFormService;
    @Autowired
    private TransferFormService transferFormService;
    @Autowired
    private OtpConfirmationService otpConfirmationService;


    public List<User> getUsersByDepartment(String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String id = user.getId();
        String prefix = id.substring(0, id.length() - 7);
        List<User> users = new ArrayList<>();
        if(user.getRole() == Role.USER){
            users.addAll(userRepo.findByFacultyAndJobType(user.getFaculty(), "Dean"));
        }
        users.addAll(userRepo.findByIdStartingWithAndVerified(prefix, true));
        return users;
    }

    public UserInfoResponse getUser(String token) throws UserNotFoundException {
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String imageBase64 = null;
        if (user.getImage_data() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(user.getImage_data());
        }
        return UserInfoResponse.builder()
                .id(user.getId())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .date_of_birth(user.getDate_of_birth())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone_no(user.getPhone_no())
                .address(user.getAddress())
                .city(user.getCity())
                .postal_code(user.getPostal_code())
                .ic_no(user.getIc_no())
                .emp_id(user.getEmp_id())
                .job_type(user.getJobType())
                .department(user.getDepartment())
                .faculty(user.getFaculty())
                .image_type(user.getImage_type())
                .image_name(user.getImage_name())
                .image_data(imageBase64)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isLogin(true)
                .role(user.getRole() != null? user.getRole().toString(): "none")
                .build();
    }

    public User updateProfile(String header, RegisterDTO registerDTO, MultipartFile image) throws IOException, UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        user.setFirst_name(registerDTO.getFirst_name());
        user.setLast_name(registerDTO.getLast_name());
        user.setDate_of_birth(registerDTO.getDate_of_birth());
        user.setGender(registerDTO.getGender());
        user.setPhone_no(registerDTO.getPhone_no());
        user.setAddress(registerDTO.getAddress());
        user.setCity(registerDTO.getCity());
        user.setPostal_code(registerDTO.getPostal_code());
        user.setIc_no(registerDTO.getIc_no());
        user.setImage_name(image != null? image.getOriginalFilename() : user.getImage_name());
        user.setImage_type(image != null? image.getContentType(): user.getImage_type());
        user.setImage_data(image != null? image.getBytes(): user.getImage_data());
        user.setUpdatedAt(new Date());

        userRepo.save(user);
        return user;
    }

    public String resetPassword(String header, SecurityDTO resetPasswordDTO) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));
        String oldPassword = resetPasswordDTO.getOld_password();
        String newPassword = resetPasswordDTO.getNew_password();

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            if (!passwordEncoder.matches(newPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);
                return "Reset success";
            }
            return "Can't be same password";
        }
        return "Reset rejected";
    }


    @Transactional
    public String deleteAccount(String header, SecurityDTO deleteAccountDTO) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));
        String oldPassword = deleteAccountDTO.getPassword_for_delete();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalStateException("Password not matched");
        }

        forumRepo.deleteByUserId(user.getId());
        registerConfirmationTokenRepo.deleteByUserId(user.getId());
        normalLeaveFormService.deleteForm(user.getId());
        accidentLeaveFormService.deleteForm(user.getId());
        paternalLeaveFormService.deleteForm(user.getId());
        maternityLeaveFormService.deleteForm(user.getId());
        medicalLeaveFormService.deleteForm(user.getId());
        userRepo.delete(user);
        return "delete success";
    }

    public List<Object> getAllAppliedLeaveForms(String header) {
        List<Object> forms = new ArrayList<>();
        forms.addAll(normalLeaveFormService.getFormsOfUser(header));
        forms.addAll(accidentLeaveFormService.getFormsOfUser(header));
        forms.addAll(paternalLeaveFormService.getFormsOfUser(header));
        forms.addAll(maternityLeaveFormService.getFormsOfUser(header));
        forms.addAll(medicalLeaveFormService.getFormsOfUser(header));

        return forms;
    }

    public List<TransferForm> getAllTransferForms(String header) {
        return transferFormService.getFormsOfUser(header);
    }

    public String sendOTP(String email) throws UserNotFoundException {
        if (!userRepo.existsByEmail(email)) {
            throw new IllegalStateException("Email not matched");
        }
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));
        user.setVerified(false);
        userRepo.save(user);
        otpConfirmationService.sendOtp(user, email);
        return "success";
    }

    public String confirmOTP(Integer otp) throws UserNotFoundException {
        String userEmail = otpConfirmationService.getUserEmail(otp);
        if(userEmail == null){
            throw new UserNotFoundException("User not found with this OTP");
        }
        User user = userRepo.findByEmail(userEmail).
                orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String status = otpConfirmationService.confirmOtp(otp);
        if(status == "OTP confirmed successfully"){
            user.setVerified(true);
            userRepo.save(user);
            return "success";
        }
        return "OTP not confirmed";
    }

    public String resetForForgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws UserNotFoundException {
        String email = forgotPasswordDTO.getEmail();

        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));
        String newPassword = forgotPasswordDTO.getNewPassword();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return "Can't be same password";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return "Reset success";
    }

    public List<Object> getAllAppliedLeaveFormsById(String id) throws UserNotFoundException {
        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        List<Object> forms = new ArrayList<>();
        forms.addAll(normalLeaveFormService.getFormsOfUserById(id));
        forms.addAll(accidentLeaveFormService.getFormsOfUserById(id));
        forms.addAll(paternalLeaveFormService.getFormsOfUserById(id));
        forms.addAll(maternityLeaveFormService.getFormsOfUserById(id));
        forms.addAll(medicalLeaveFormService.getFormsOfUserById(id));
        return forms;
    }
}
