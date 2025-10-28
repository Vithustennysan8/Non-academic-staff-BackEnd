package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RegisterConfirmationTokenService {

    private final RegisterConfirmationTokenRepo confirmationTokenRepo;
    private final ExtractUserService extractUserService;

    public String createToken(User user) {

        String token = UUID.randomUUID().toString();
        RegisterConfirmationToken confirmationToken = RegisterConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .user(user)
                .build();

        confirmationTokenRepo.save(confirmationToken);
        return token;
    }

    public User confirm(String token) {
        RegisterConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(confirmationToken);
        return confirmationToken.getUser();
    }

    public List<RegisterConfirmationToken> getVerifyRequests(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if(user == null || user.getRole() != Role.ADMIN) {
            return Collections.emptyList();
        }
        String prefix = user.getId().substring(0, user.getId().length() - 7);
        return confirmationTokenRepo.findByUserIdPrefixAndVerificationStatus(prefix, false);
    }

    public List<RegisterConfirmationToken> getVerifyAdminRegisterRequests(String header)
            throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if(user == null || user.getRole() != Role.SUPER_ADMIN) {
            return Collections.emptyList();
        }
        List<RegisterConfirmationToken> byRoleAndVerificationStatus = new ArrayList<>();
        byRoleAndVerificationStatus.addAll(confirmationTokenRepo.findByRoleAndVerificationStatus(Role.ADMIN, false));
        byRoleAndVerificationStatus.addAll(confirmationTokenRepo.findByRoleAndVerificationStatus(Role.MANAGER, false));
        byRoleAndVerificationStatus.addAll(confirmationTokenRepo.findByRoleAndVerificationStatus(Role.USER, false));
        return byRoleAndVerificationStatus;

    }
}
