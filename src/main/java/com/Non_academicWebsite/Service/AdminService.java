package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.*;
import com.Non_academicWebsite.Repository.UserRepo;
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
        forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
        forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));

        return forms;

    }
}
