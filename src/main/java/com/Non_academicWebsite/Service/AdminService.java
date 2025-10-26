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
    @Autowired
    private ExtractUserService extractUserService;


    public List<Object> getAllLeaveFormRequests(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        String prefix = extractUserService.getTheIdPrefixByUser(user);

        List<Object> forms = new ArrayList<>();
        switch (user.getJobType()) {
            case "Head of the Department" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWith(prefix));
                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
                forms.addAll(accidentLeaveFormRepo.findByFacultyAndHeadStatus(user.getFaculty(), "Accepted"));
                forms.addAll(paternalLeaveFormRepo.findByFacultyAndHeadStatus(user.getFaculty(), "Accepted"));
                forms.addAll(medicalLeaveFromRepo.findByFacultyAndHeadStatus(user.getFaculty(), "Accepted"));
                forms.addAll(maternityLeaveFormRepo.findByFacultyAndHeadStatus(user.getFaculty(), "Accepted"));
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
//                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWith(prefix));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWith(prefix));
//                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWith(prefix));
//                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWith(prefix));
//                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWith(prefix));
            }
            case "Dean" -> {
//                forms.addAll(accidentLeaveFormRepo.findByFaculty(user.getFaculty()));
                forms.addAll(normalLeaveFormRepo.findByFaculty(user.getFaculty()));
//                forms.addAll(paternalLeaveFormRepo.findByFaculty(user.getFaculty()));
//                forms.addAll(medicalLeaveFromRepo.findByFaculty(user.getFaculty()));
//                forms.addAll(maternityLeaveFormRepo.findByFaculty(user.getFaculty()));
            }
            case "Chief Medical Officer" -> {
//                forms.addAll(accidentLeaveFormRepo.findAll());
//                forms.addAll(medicalLeaveFromRepo.findAll());
//                forms.addAll(maternityLeaveFormRepo.findAll());
            }
            case "Registrar" -> {
//                forms.addAll(medicalLeaveFromRepo.findAll());
//                forms.addAll(maternityLeaveFormRepo.findAll());
            }
            case "Non Academic Establishment Division" -> {
//                forms.addAll(paternalLeaveFormRepo.findAll());
//                forms.addAll(accidentLeaveFormRepo.findAll());
//                forms.addAll(medicalLeaveFromRepo.findAll());
//                forms.addAll(maternityLeaveFormRepo.findAll());
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
        switch (user.getJobType()) {
            case "Head of the Department" -> {
                forms.addAll(accidentLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
                forms.addAll(normalLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
                forms.addAll(paternalLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
                forms.addAll(medicalLeaveFromRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
                forms.addAll(maternityLeaveFormRepo.findByUserIdStartingWithAndHeadStatus(prefix, "pending"));
            }
            case "Dean" -> {
                forms.addAll(accidentLeaveFormRepo.findByFacultyAndHeadStatusAndDeanStatus(user.getFaculty(),
                        "Accepted", "pending"));
                forms.addAll(paternalLeaveFormRepo.findByFacultyAndHeadStatusAndDeanStatus(user.getFaculty(),
                        "Accepted", "pending"));
                forms.addAll(medicalLeaveFromRepo.findByFacultyAndHeadStatusAndDeanStatus(user.getFaculty(),
                        "Accepted", "pending"));
                forms.addAll(maternityLeaveFormRepo.findByFacultyAndHeadStatusAndDeanStatus(user.getFaculty(),
                        "Accepted", "pending"));
            }
            case "Chief Medical Officer" -> {
                forms.addAll(accidentLeaveFormRepo.findByDeanStatusAndCmoStatus("Accepted", "pending"));
                forms.addAll(medicalLeaveFromRepo.findByDeanStatusAndCmoStatus("Accepted", "pending"));
                forms.addAll(maternityLeaveFormRepo.findByDeanStatusAndCmoStatus("Accepted","pending"));
            }
            case "Registrar" -> {
                forms.addAll(medicalLeaveFromRepo.findByCmoStatusAndRegistrarStatus("Accepted", "pending"));
                forms.addAll(maternityLeaveFormRepo.findByCmoStatusAndRegistrarStatus("Accepted", "pending"));
            }
            case "Non Academic Establishment Division" -> {
                forms.addAll(paternalLeaveFormRepo.findByDeanStatusAndNaeStatus("Accepted", "pending"));
                forms.addAll(accidentLeaveFormRepo.findByCmoStatusAndNaeStatus("Accepted", "pending"));
                forms.addAll(medicalLeaveFromRepo.findByRegistrarStatusAndNaeStatus("Accepted", "pending"));
                forms.addAll(maternityLeaveFormRepo.findByRegistrarStatusAndNaeStatus("Accepted", "pending"));
            }
            default -> {
                forms.addAll(Collections.emptyList());
            }
        }
        return forms;
    }

}
