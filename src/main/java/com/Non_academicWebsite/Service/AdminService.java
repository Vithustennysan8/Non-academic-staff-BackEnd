package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.UserNotFoundException;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.*;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private PaternalLeaveFormRepo paternalLeaveFormRepo;
    @Autowired
    private MedicalLeaveFromRepo medicalLeaveFromRepo;
    @Autowired
    private MaternityLeaveFormRepo maternityLeaveFormRepo;
    @Autowired
    private TransferFormRepo transferFormRepo;
    @Autowired
    private RegisterConfirmationTokenService confirmationTokenService;
    @Autowired
    private RegisterConfirmationTokenRepo registerConfirmationTokenRepo;
    @Autowired
    private ForumRepo forumRepo;

    public List<Object> getAllLeaveFormRequests(String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String userid = user.getId();
        String prefix = userid.substring(0, userid.length()-7);

        List<Object> forms = new ArrayList<>();
        switch (user.getJob_type()) {
            case "Head of the Department" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWith(prefix));
                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted"));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted"));
                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted"));
                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted"));
                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted"));
            }
            case "Chief Medical Officer" -> {
                forms.addAll(accidentLeaveFormRepo.findByDeanStatus("Accepted"));
                forms.addAll(medicalLeaveFromRepo.findByDeanStatus("Accepted"));
                forms.addAll(maternityLeaveFormRepo.findByDeanStatus("Accepted"));
            }
            case "Registrar" -> {
                forms.addAll(medicalLeaveFromRepo.findByCmoStatus("Accepted"));
                forms.addAll(maternityLeaveFormRepo.findByCmoStatus("Accepted"));
            }
            case "Non Academic Establishment Division" -> {
                forms.addAll(paternalLeaveFormRepo.findByDeanStatus("Accepted"));
                forms.addAll(accidentLeaveFormRepo.findByCmoStatus("Accepted"));
                forms.addAll(medicalLeaveFromRepo.findByRegistrarStatus("Accepted"));
                forms.addAll(maternityLeaveFormRepo.findByRegistrarStatus("Accepted"));
            }
            default -> {
                forms.addAll(Collections.emptyList());
            }
        }
        return forms;
    }

    public List<TransferForm> getAllTransferFormRequests(String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String userid = user.getId();
        String prefix = userid.substring(0, userid.length()-7);

        switch (user.getJob_type()) {
            case "Head of the Department" -> {
                return transferFormRepo.findByUserIdStartingWith(prefix);
            }
            case "Dean" -> {
                return transferFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "Accepted");
            }
            case "Registrar" -> {
                return transferFormRepo.findByDeanStatus("Accepted");
            }
            case "Non Academic Establishment Division" -> {
                return transferFormRepo.findByRegistrarStatus("Accepted");
            }
            case "RegistrarApproval" -> {
                return transferFormRepo.findByNaeStatus("Accepted");
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Transactional
    public Object deleteUserById(String id, String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        if(userRepo.existsById(id)){
            forumRepo.deleteByUserId(id);
            registerConfirmationTokenRepo.deleteByUserId(id);
            userRepo.deleteById(id);
        }
        return confirmationTokenService.getVerifyRequests(header);
    }

    public List<Object> getAllLeaveForms(String header) throws UserNotFoundException {
        String token = header.substring(7);
        String email = jwtService.extractUserEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User is not found!!!"));

        String userId = user.getId();
        String prefix = userId.substring(0, userId.length() - 7);

        List<Object> forms = new ArrayList<>();
        switch (user.getJob_type()) {
            case "Head of the Department" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWith(prefix));
                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
                forms.addAll(accidentLeaveFormRepo.findByFacultyAndRole(user.getFaculty(), Role.ADMIN));
                forms.addAll(normalLeaveFormRepo.findByFacultyAndRole(user.getFaculty(), Role.ADMIN));
                forms.addAll(paternalLeaveFormRepo.findByFacultyAndRole(user.getFaculty(), Role.ADMIN));
                forms.addAll(medicalLeaveFromRepo.findByFacultyAndRole(user.getFaculty(), Role.ADMIN));
                forms.addAll(maternityLeaveFormRepo.findByFacultyAndRole(user.getFaculty(), Role.ADMIN));
            }
            case "Chief Medical Officer" -> {
                forms.addAll(accidentLeaveFormRepo.findAll());
                forms.addAll(medicalLeaveFromRepo.findAll());
                forms.addAll(maternityLeaveFormRepo.findAll());
            }
            case "Registrar" -> {
                forms.addAll(medicalLeaveFromRepo.findAll());
                forms.addAll(maternityLeaveFormRepo.findAll());
            }
            case "Non Academic Establishment Division" -> {
                forms.addAll(paternalLeaveFormRepo.findAll());
                forms.addAll(accidentLeaveFormRepo.findAll());
                forms.addAll(medicalLeaveFromRepo.findAll());
                forms.addAll(maternityLeaveFormRepo.findAll());
            }
            default -> {
                forms.addAll(Collections.emptyList());
            }
        }
        return forms;
    }
}
