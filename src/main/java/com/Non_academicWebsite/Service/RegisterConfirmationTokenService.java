package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterConfirmationTokenService {
    @Autowired
    private RegisterConfirmationTokenRepo confirmationTokenRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ExtractUserService extractUserService;

    public String createToken(User user) {

        String token = UUID.randomUUID().toString();
        RegisterConfirmationToken confirmationToken = RegisterConfirmationToken.builder()
                .token(token)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .user(user)
                .build();

        confirmationTokenRepo.save(confirmationToken);
        return token;
    }

    public User confirm(String token) throws UserNotFoundException {
        RegisterConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token);
        confirmationToken.setConfirmedAt(new Date(System.currentTimeMillis()));
        confirmationTokenRepo.save(confirmationToken);
        return confirmationToken.getUser();
    }

    public List<RegisterConfirmationToken> getVerifyRequests(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if(user == null || user.getRole() != Role.ADMIN) {
            return Collections.emptyList();
        }
        String prefix = user.getId().substring(0, user.getId().length() - 7);
        return confirmationTokenRepo.findByUserIdPrefixAndVerificationStatus(prefix, false);
    }

    public List<RegisterConfirmationToken> getVerifyAdminRegisterRequests(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if(user == null || user.getRole() != Role.SUPER_ADMIN) {
            return Collections.emptyList();
        }
        return confirmationTokenRepo.findByRoleAndVerificationStatus(Role.ADMIN, false);
    }
}
