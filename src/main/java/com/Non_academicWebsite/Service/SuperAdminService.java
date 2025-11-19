package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final ExtractUserService extractUserService;
    private final UserRepo userRepo;
    private final RegisterConfirmationTokenService confirmationTokenService;

    public List<RegisterConfirmationToken> getVerifyAdminRegisterRequests(String header) throws ResourceNotFoundException {
        return  confirmationTokenService.getVerifyAdminRegisterRequests(header);
    }

    public String deleteUser(String id, String header) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if(user.getRole() != Role.SUPER_ADMIN){
            throw new UnauthorizedAccessException("Permission denied");
        }

        if(!userRepo.existsById(id)){ throw new ResourceNotFoundException("User not found");}
        userRepo.deleteById(id);
        return "User deleted successfully";
    }

    @Transactional
    public Object deleteRegisterUser(String id, String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(userRepo.existsById(id)){throw new ResourceNotFoundException("User not found");}
            userRepo.deleteById(id);
        return confirmationTokenService.getVerifyAdminRegisterRequests(header);
    }
}
