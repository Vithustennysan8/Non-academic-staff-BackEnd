package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.DTO.SecurityDTO;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Date;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(String token) {
        String email = jwtService.extractUserEmail(token);
        return userRepo.findByEmail(email).orElseThrow();
    }

    public void updateProfile(String header, RegisterDTO registerDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(registerDTO.getEmail()).orElseThrow();
        user.setFirst_name(registerDTO.getFirst_name());
        user.setLast_name(registerDTO.getLast_name());
        user.setDate_of_birth(registerDTO.getDate_of_birth());
        user.setGender(registerDTO.getGender());
        user.setEmail(registerDTO.getEmail());
        user.setPhone_no(registerDTO.getPhone_no());
        user.setAddress(registerDTO.getAddress());
        user.setCity(registerDTO.getCity());
        user.setPostal_code(registerDTO.getPostal_code());
        user.setIc_no(registerDTO.getIc_no());
        user.setEmp_id(registerDTO.getEmp_id());
        user.setJob_type(registerDTO.getJob_type());
        user.setDepartment(registerDTO.getDepartment());
        user.setFaculty(registerDTO.getFaculty());
        user.setUpdatedAt(new Date());

        userRepo.save(user);
    }

    public String resetPassword(String header, SecurityDTO resetPasswordDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow();
        String oldPassword = resetPasswordDTO.getOld_password();
        String newPassword = resetPasswordDTO.getNew_password();

        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            if(!passwordEncoder.matches(newPassword, user.getPassword())){
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);
                return "reset success";
            }
            return "can't be same password";
        }
        return "reset rejected";
    }


    public String deleteAccount(String header, SecurityDTO deleteAccountDTO) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);

        User user = userRepo.findByEmail(email).orElseThrow();
        String oldPassword = deleteAccountDTO.getPassword_for_delete();

        if(passwordEncoder.matches(oldPassword,user.getPassword())){
                userRepo.delete(user);
                return "delete success";
        }
        return "delete rejected";
    }
}
