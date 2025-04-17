package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.CustomException.UserAlreadyExistsException;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.CustomIdGenerator.UserIdGenerator;
import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
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
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private ExtractUserService extractUserService;

    @Transactional
    public Boolean registerStaff(RegisterDTO registerDTO, MultipartFile image) throws IOException, UserAlreadyExistsException {
        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            throw new UserAlreadyExistsException("User Already found with "+registerDTO.getEmail()+" emailId!!!");
        }
        String customId = userIdGenerator.generateCustomUserID(registerDTO.getFacultyId(), registerDTO.getDepartment(), registerDTO.getJob_type());
        Faculty fac = facultyService.getFac(registerDTO.getFacultyId());

//        Role role = null;
//        switch (registerDTO.getRole()){
//            case USER -> role = Role.USER;
//            case MANAGER -> role = Role.MANAGER;
//            case ADMIN -> role = Role.ADMIN;
//            case SUPER_ADMIN -> role = Role.SUPER_ADMIN;
//        }

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
                .faculty(fac.getFacultyName())
                .createdAt(new Date())
                .updatedAt(new Date())
                .image_type(image != null ? image.getContentType() : null)
                .image_name(image != null ? image.getOriginalFilename() : null)
                .image_data(image != null ? image.getBytes() : null)
                .role(registerDTO.getRole())
                .verified(false)
                .build();

        userRepo.save(user);

        String confirmationToken = confirmationTokenService.createToken(user);
        System.out.println(confirmationToken);

        String url = "http://localhost:8080/api/auth/verify?token=" + confirmationToken;

        return true;
    }


    public AuthenticationResponse login(LoginDTO loginDTO) throws UserNotFoundException, UnauthorizedAccessException {
        User user = userRepo.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new UserNotFoundException("User not found with "+loginDTO.getEmail()+" this email!!!"));

        if (!user.isVerified()){
             throw new UnauthorizedAccessException("User registration not verified yet!");
        }

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


        var jwtToken = jwtService.generateToken(user, user.getRole());

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .message("logged")
                .build();
    }

    public List<RegisterConfirmationToken> confirmUser(String confirmationToken, String header) throws UserNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }

        User requestedUser = confirmationTokenService.confirm(confirmationToken) ;
        if (requestedUser != null) {
            if (Role.ADMIN == user.getRole() || Role.SUPER_ADMIN == user.getRole()){
                requestedUser.setVerified(true);
                userRepo.save(requestedUser);
                mailService.sendMailForRegister(requestedUser.getEmail(), "http://localhost:5173/login", requestedUser,
                        user.getFirst_name()+" "+user.getLast_name());
            }else {
                return Collections.emptyList();
            }
        }

        if (Objects.equals(user.getRole(), Role.SUPER_ADMIN))
            return confirmationTokenService.getVerifyAdminRegisterRequests(header);

        return confirmationTokenService.getVerifyRequests(header);
    }
}
