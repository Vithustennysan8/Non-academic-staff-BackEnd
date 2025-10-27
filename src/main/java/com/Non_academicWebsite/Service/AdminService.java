package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.Entity.Forms.Forms;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.*;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NormalLeaveFormRepo normalLeaveFormRepo;
    @Autowired
    private TransferFormRepo transferFormRepo;
    @Autowired
    private RegisterConfirmationTokenService confirmationTokenService;
    @Autowired
    private RegisterConfirmationTokenRepo registerConfirmationTokenRepo;
    @Autowired
    private ForumRepo forumRepo;
    @Autowired
    private ExtractUserService extractUserService;


    public List<Object> getAllLeaveFormRequests(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);

        List<Object> forms = new ArrayList<>();
        if (user.getJobType().equals("Head of the Department")) {
            forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
        } else {
            forms.addAll(Collections.emptyList());
        }
        return forms;
    }

    public List<TransferForm> getAllTransferFormRequests(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);

        switch (user.getJobType()) {
            case "Head of the Department" -> {
                return transferFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending");
            }
            case "Dean" -> {
                return transferFormRepo.findByFacultyAndHeadStatusAndDeanStatus(user.getFaculty(), "Accepted", "pending");
            }
            case "Registrar" -> {
                return transferFormRepo.findByDeanStatusAndRegistrarStatus("Accepted", "pending");
            }
            case "Non Academic Establishment Division" -> {
                return transferFormRepo.findByRegistrarStatusAndNaeStatus("Accepted", "pending");
            }
            case "RegistrarApproval" -> {
                return transferFormRepo.findByNaeStatusAndRegistrarApprovalStatus("Accepted", "pending");
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Transactional
    public Object deleteUserById(String id, String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(userRepo.existsById(id)){
            forumRepo.deleteByUserId(id);
            registerConfirmationTokenRepo.deleteByUserId(id);
            userRepo.deleteById(id);
        }
        return confirmationTokenService.getVerifyRequests(header);
    }

    public List<Forms> getAllLeaveForms(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);

        List<Forms> forms = new ArrayList<>();
        switch (user.getJobType()) {
            case "Head of the Department" -> {
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
                forms.addAll(normalLeaveFormRepo.findByFaculty(user.getFaculty()));
            }
            default -> {
                forms.addAll(Collections.emptyList());
            }
        }
        return forms;
    }

//    public List<Object> getFormsSummaryForDean(String header) throws UserNotFoundException {
//        List<Forms> results = getAllLeaveForms(header);
//
//        Map<String, Object> info = new HashMap<>();
//        results.stream().forEach(result -> {
//            String department = result.getUser().getDepartment();
//            String formType = result.getFormType();
//            String year = result.getLeaveAt().toString().substring(0,4);
//            String month = result.getLeaveAt().toString().substring(5,7);
//
//            if(info.containsKey(department)){
//                Object deatil = info.get(department);
////                if(deatil.year == year && deatil.month == month){
//
//                }
//            }
//        });
//
//
//        return null;
//    }

    public List<Object> getPendingLeaveFormRequestsByApprover(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);


        List<Object> forms = new ArrayList<>();
        if (user.getJobType().equals("Head of the Department")) {
            forms.addAll(normalLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
        } else {
            forms.addAll(Collections.emptyList());
        }
        return forms;
    }

}
