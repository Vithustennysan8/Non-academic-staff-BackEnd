package com.Non_academicWebsite.Service.ExtractUser;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExtractUserService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;


    public User extractUserByAuthorizationHeader(String authorizationHeader) throws ResourceNotFoundException {
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUserEmail(token);
        return userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email "
                + email));
    }
    public String getTheIdPrefixByUser(User user) {
        String userId = user.getId();
        return userId.substring(0, userId.length()-7);
    }

    public String getTheIdPrefixByAuthorizationHeader(String authorizationHeader) throws ResourceNotFoundException {
        User user = extractUserByAuthorizationHeader(authorizationHeader);
        return getTheIdPrefixByUser(user);
    }

}
