package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UserAlreadyExistsException;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.CustomIdGenerator.UserIdGenerator;
import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserIdGenerator userIdGenerator;
    @Autowired
    private RegisterConfirmationTokenService confirmationTokenService;
    @Autowired
    private MailService mailService;

    @Transactional
    public Boolean registerStaff(RegisterDTO registerDTO, MultipartFile image) throws IOException, UserAlreadyExistsException {
        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            throw new UserAlreadyExistsException("User Already found with "+registerDTO.getEmail()+" emailId!!!");
        }
        String customId = userIdGenerator.generateCustomUserID(registerDTO.getFaculty(), registerDTO.getDepartment(), registerDTO.getJob_type());

        User user = User.builder()
                .id(customId)
                .first_name(registerDTO.getFirst_name())
                .last_name(registerDTO.getLast_name())
                .date_of_birth(registerDTO.getDate_of_birth())
                .gender(registerDTO.getGender())
                .email(registerDTO.getEmail())
                .app_password(registerDTO.getApp_password())
                .phone_no(registerDTO.getPhone_no())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .city(registerDTO.getCity())
                .postal_code(registerDTO.getPostal_code())
                .ic_no(registerDTO.getIc_no())
                .emp_id(registerDTO.getEmp_id())
                .jobType(registerDTO.getJob_type())
                .department(registerDTO.getDepartment())
                .faculty(registerDTO.getFaculty())
                .createdAt(new Date())
                .updatedAt(new Date())
                .image_type(image != null ? image.getContentType() : null)
                .image_name(image != null ? image.getOriginalFilename() : null)
                .image_data(image != null ? image.getBytes() : null)
                .role(registerDTO.getRole())
//                .role(Role.USER)
                .verified(false)
                .build();

        userRepo.save(user);

        String confirmationToken = confirmationTokenService.createToken(user);
        System.out.println(confirmationToken);

        String url = "http://localhost:8080/api/auth/verify?token=" + confirmationToken;
//        mailService.sendMail("vithustennysan21@gmail.com", url, user.getFirst_name(), user.getDepartment(), user.getFaculty());

        return true;
    }


    public AuthenticationResponse login(LoginDTO loginDTO) throws UserNotFoundException {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
        }catch (Exception ex){
            throw new UserNotFoundException("User not found with "+loginDTO.getEmail()+" this email!!!");
        }

        User user = userRepo.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new UserNotFoundException("User not found with "+loginDTO.getEmail()+" this email!!!"));

        var jwtToken = jwtService.generateToken(user, user.getRole());

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .message("logged")
                .build();
    }

    public List<RegisterConfirmationToken> confirmUser(String confirmationToken, String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if(user == null) {
            return Collections.emptyList();
        }

        User requestedUser = confirmationTokenService.confirm(confirmationToken) ;
        if (requestedUser != null) {
            if (Objects.equals(user.getJobType(), "Head of the Department") || Objects.equals(user.getJobType(), "Dean")){
                requestedUser.setVerified(true);
                userRepo.save(requestedUser);
                mailService.sendMailForRegister(requestedUser.getEmail(), "http://localhost:5173/login", requestedUser, user.getFirst_name()+" "+user.getLast_name());
            }else {
                return Collections.emptyList();
            }
        }
        return confirmationTokenService.getVerifyRequests(header);
    }
}
