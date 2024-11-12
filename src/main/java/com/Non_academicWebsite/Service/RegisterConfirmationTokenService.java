package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RegisterConfirmationTokenService {
    @Autowired
    private RegisterConfirmationTokenRepo confirmationTokenRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

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
//        assert confirmationToken != null;
        confirmationToken.setConfirmedAt(new Date(System.currentTimeMillis()));
        confirmationTokenRepo.save(confirmationToken);
        return confirmationToken.getUser();
    }

    public List<RegisterConfirmationToken> getVerifyRequests(String header) {
        String token = header.substring(7); // The part of the header after "Bearer "
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);
        if(user == null || user.getRole()!= Role.ADMIN) {
            return Collections.emptyList();
        }
        String prefix = user.getId().substring(0, user.getId().length() - 7);
        return confirmationTokenRepo.findByUserIdPrefixAndVerificationStatus(prefix, false);
    }
}
