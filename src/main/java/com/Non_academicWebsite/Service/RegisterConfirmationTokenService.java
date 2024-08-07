package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class RegisterConfirmationTokenService {
    @Autowired
    private RegisterConfirmationTokenRepo confirmationTokenRepo;

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

    public User confirm(String token) {
        RegisterConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token).orElse(null);
        if (confirmationToken != null) {
            confirmationToken.setConfirmedAt(new Date(System.currentTimeMillis()));
            confirmationTokenRepo.save(confirmationToken);
            return confirmationToken.getUser();
        }
        return null;
    }
}
