package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.CustomIdGenerator.UserIdGenerator;
import com.Non_academicWebsite.DTO.LoginDTO;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.Entity.*;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.JobPositionRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Response.AuthenticationResponse;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserIdGenerator userIdGenerator;
    private final RegisterConfirmationTokenService confirmationTokenService;
    private final MailService mailService;
    private final FacultyService facultyService;
    private final ExtractUserService extractUserService;
    private final JobPositionRepo jobPositionRepo;

    @Transactional
    public Boolean registerStaff(RegisterDTO registerDTO, MultipartFile image) throws IOException, ResourceExistsException, ResourceNotFoundException {
        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            throw new ResourceExistsException("User Already found with "+registerDTO.getEmail()+" emailId!!!");
        }
        String customId = userIdGenerator.generateCustomUserID(registerDTO.getFacultyId(), registerDTO.getDepartment(), registerDTO.getJob_type());
        Faculty fac = facultyService.getFac(registerDTO.getFacultyId());

        JobPosition jobPosition = jobPositionRepo.findByJobPositionName(registerDTO.getJob_type()).orElse(null);
        if (jobPosition == null) {
            throw new ResourceNotFoundException("Job position not found");
        }

        User user = User.builder()
                .id(customId)
                .first_name(registerDTO.getFirst_name())
                .last_name(registerDTO.getLast_name())
                .date_of_birth(registerDTO.getDate_of_birth())
                .gender(registerDTO.getGender())
                .email(registerDTO.getEmail())
                .phone_no(registerDTO.getPhone_no())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .city(registerDTO.getCity())
                .postal_code(registerDTO.getPostal_code())
                .ic_no(registerDTO.getIc_no())
                .emp_id(registerDTO.getEmp_id())
                .jobType(registerDTO.getJob_type())
                .jobScope(jobPosition.getJobScope())
                .department(registerDTO.getDepartment())
                .faculty(fac.getFacultyName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .image_type(image != null ? image.getContentType() : null)
                .image_name(image != null ? image.getOriginalFilename() : null)
                .image_data(image != null ? image.getBytes() : null)
                .role(registerDTO.getRole())
                .verified(false)
                .build();

        userRepo.save(user);

        String confirmationToken = confirmationTokenService.createToken(user);
        System.out.println(confirmationToken);

        return true;
    }


    public AuthenticationResponse login(LoginDTO loginDTO) throws UnauthorizedAccessException, ResourceNotFoundException {
        User user = userRepo.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with " + loginDTO.getEmail() + " this email!!!"));

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
            throw new ResourceNotFoundException("username or password is incorrect");
        }


        var jwtToken = jwtService.generateToken(user, user.getRole());

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .user(user)
                .message("logged")
                .build();
    }

    public List<RegisterConfirmationToken> confirmUser(String confirmationToken, String header)
            throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(user == null) {
            return Collections.emptyList();
        }

        User requestedUser = confirmationTokenService.confirm(confirmationToken) ;
        if (requestedUser != null) {
            if (Role.ADMIN == user.getRole() || Role.SUPER_ADMIN == user.getRole()){
                requestedUser.setVerified(true);
                userRepo.save(requestedUser);
//              // TODO: change the url to the frontEnd url
                try{
                    mailService.sendMailForRegister(requestedUser.getEmail(), "http://localhost:5173/login", requestedUser,
                            user.getFirst_name()+" "+user.getLast_name());
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }else {
                return Collections.emptyList();
            }
        }

        if (Objects.equals(user.getRole(), Role.SUPER_ADMIN))
            return confirmationTokenService.getVerifyAdminRegisterRequests(header);

        return confirmationTokenService.getVerifyRequests(header);
    }

    public AuthenticationResponse refresh(Map<String, String> tokenObj) throws ResourceNotFoundException {
        String token = tokenObj.get("token");
        if(token == null){
            throw new ResourceNotFoundException("Token not found");
        }
        if(jwtService.isTokenExpired(token)){
            throw new ResourceNotFoundException("Token expired found");
        }
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found with email " + email));

        var jwtToken = jwtService.generateToken(user, user.getRole());
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .user(user)
                .message("refreshed")
                .build();

    }
}
