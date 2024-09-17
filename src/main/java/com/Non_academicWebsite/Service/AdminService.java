package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.*;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NormalLeaveFormRepo normalLeaveFormRepo;
    @Autowired
    private AccidentLeaveFormRepo accidentLeaveFormRepo;
    @Autowired
    private TransferFormRepo transferFormRepo;
    @Autowired
    private RegisterConfirmationTokenService confirmationTokenService;
    @Autowired
    private RegisterConfirmationTokenRepo registerConfirmationTokenRepo;
    @Autowired
    private ForumRepo forumRepo;

    public List<Object> getAllFormRequests(String header) {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null){
            return Collections.emptyList();
        }

        String userid = user.getId();
        String prefix = userid.substring(0, userid.length()-7);

        List<Object> forms = new ArrayList<>();
        switch (user.getJob_type()) {
            case "Head of the Department" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWithAndApproverOneStatus(prefix, "Accepted"));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWithAndApproverOneStatus(prefix, "Accepted"));
            }
        }

        return forms;

    }

    @Transactional
    public Object deleteUserById(String id, String header) {
        if(userRepo.existsById(id)){
            forumRepo.deleteByUserId(id);
            registerConfirmationTokenRepo.deleteByUserId(id);
            userRepo.deleteById(id);
        }
        return confirmationTokenService.getVerifyRequests(header);
    }
}
