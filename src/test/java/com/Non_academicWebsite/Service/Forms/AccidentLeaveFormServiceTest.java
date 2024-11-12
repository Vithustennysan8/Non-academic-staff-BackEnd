package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.AccidentLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.AccidentLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class AccidentLeaveFormServiceTest {
    @InjectMocks
    private AccidentLeaveFormService accidentLeaveFormService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private AccidentLeaveFormRepo accidentLeaveFormRepo;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // Test for the add() method
    @Test
    void testAddAccidentLeaveForm() throws IOException {
        String header = "Bearer token";
        String token = "token";
        String email = "user@example.com";

        AccidentLeaveFormDTO formDTO = new AccidentLeaveFormDTO();
        formDTO.setAccidentOccurredDuring("Work");
        formDTO.setDateAndTimeOfAccident(new Date());
        formDTO.setPlaceOFAccident("Factory");
        // Set other fields as necessary

        User user = new User();
        user.setId("userId");

        AccidentLeaveForm savedForm = new AccidentLeaveForm();

        when(jwtService.extractUserEmail(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.save(any(AccidentLeaveForm.class))).thenReturn(savedForm);

        AccidentLeaveForm result = accidentLeaveFormService.add(header, formDTO, null);

        assertNotNull(result);
        verify(jwtService).extractUserEmail(token);
        verify(userRepo).findByEmail(email);
        verify(accidentLeaveFormRepo).save(any(AccidentLeaveForm.class));
    }

    // Test for the getAccidentLeaveForms() method

    // Test case when user is not found
    @Test
    void testGetAccidentLeaveForms_UserNotFound() {
        String header = "Bearer token";
        String token = "token";
        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();

        when(jwtService.extractUserEmail(token)).thenReturn("user@example.com");
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.empty());

        List<AccidentLeaveForm> result = accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header);

        assertEquals(Collections.emptyList(), result);
        verify(userRepo).findByEmail("user@example.com");
    }

    // Test case for retrieving forms by user ID prefix (no department or faculty provided)
    @Test
    void testGetAccidentLeaveForms_ByUserIdPrefix() {
        String header = "Bearer token";
        String token = "token";
        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        User user = new User();
        user.setId("userId1234567"); // ID with 7 characters at the end

        when(jwtService.extractUserEmail(token)).thenReturn("user@example.com");
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.findByUserIdStartingWith("userId")).thenReturn(Collections.singletonList(new AccidentLeaveForm()));

        List<AccidentLeaveForm> result = accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accidentLeaveFormRepo).findByUserIdStartingWith("userId");
    }

     // Test case for retrieving forms by department
    @Test
    void testGetAccidentLeaveForms_ByDepartment() {
        String header = "Bearer token";
        String token = "token";
        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setDepartment("Physics");
        User user = new User();
        user.setId("userId1234567");
        user.setDepartment("Science");

        when(jwtService.extractUserEmail(token)).thenReturn("user@example.com");
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment())).thenReturn(Collections.singletonList(new AccidentLeaveForm()));

        List<AccidentLeaveForm> result = accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accidentLeaveFormRepo).findByDepartment(user.getFaculty(), reqFormsDTO.getDepartment());
    }

    // Test case for retrieving forms by faculty
    @Test
    void testGetAccidentLeaveForms_ByFaculty() {
        String header = "Bearer token";
        String token = "token";
        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setFaculty("Science");
        User user = new User();
        user.setId("userId1234567");
        user.setFaculty("Science");

        when(jwtService.extractUserEmail(token)).thenReturn("user@example.com");
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.findByFaculty(reqFormsDTO.getFaculty())).thenReturn(Collections.singletonList(new AccidentLeaveForm()));

        List<AccidentLeaveForm> result = accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accidentLeaveFormRepo).findByFaculty(reqFormsDTO.getFaculty());
    }

    // Test case for retrieving forms by both faculty and department
    @Test
    void testGetAccidentLeaveForms_ByFacultyAndDepartment() {
        String header = "Bearer token";
        String token = "token";
        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setFaculty("Science");
        reqFormsDTO.setDepartment("Physics");
        User user = new User();
        user.setId("userId1234567");
        user.setFaculty("Science");
        user.setDepartment("Physics");

        when(jwtService.extractUserEmail(token)).thenReturn("user@example.com");
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment()))
                .thenReturn(Collections.singletonList(new AccidentLeaveForm()));

        List<AccidentLeaveForm> result = accidentLeaveFormService.getAccidentLeaveForms(reqFormsDTO, header);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accidentLeaveFormRepo).findByFacultyAndDepartment(reqFormsDTO.getFaculty(), reqFormsDTO.getDepartment());
    }

    // Test for the acceptForm() method
    @Test
    void testAcceptForm_HeadOfDepartment() {
        Long formId = 1L;
        ApprovalDTO approvalDTO = new ApprovalDTO();
        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
        approvalDTO.setDescription("Approved");

        AccidentLeaveForm form = new AccidentLeaveForm();
        User user = new User();
        user.setJob_type("Head of the Department");

        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(form));
        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
        when(accidentLeaveFormRepo.save(any(AccidentLeaveForm.class))).thenReturn(form);

        Object result = accidentLeaveFormService.acceptForm(formId, approvalDTO);

        assertNotNull(result);
        assertEquals("Accepted", form.getHeadStatus());
        assertEquals(approvalDTO.getUser(), form.getHead());
        assertEquals(approvalDTO.getDescription(), form.getHeadDescription());
        verify(accidentLeaveFormRepo).save(form);
    }

//    @Test
//    void testAcceptForm_Dean() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//        approvalDTO.setDescription("Dean Approval");
//
//        AccidentLeaveForm form = new AccidentLeaveForm();
//        User user = new User();
//        user.setJob_type("Dean");
//
//        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(form));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//        when(accidentLeaveFormRepo.save(any(AccidentLeaveForm.class))).thenReturn(form);
//
//        Object result = accidentLeaveFormService.acceptForm(formId, approvalDTO);
//
//        assertNotNull(result);
////        assertEquals("Accepted", form.getHeadStatus());
//        assertEquals(approvalDTO.getUser(), form.getHead());
//        assertEquals(approvalDTO.getDescription(), form.getHeadDescription());
//        verify(accidentLeaveFormRepo).save(form);
//    }

    // Test for the rejectForm() method
//    @Test
//    void testRejectForm_HeadOfDepartment() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//        approvalDTO.setDescription("Rejection Reason");
//
//        AccidentLeaveForm form = new AccidentLeaveForm();
//        User user = new User();
//        user.setJob_type("Head of the Department");
//
//        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(form));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//        when(accidentLeaveFormRepo.save(any(AccidentLeaveForm.class))).thenReturn(form);
//
//        Object result = accidentLeaveFormService.rejectForm(formId, approvalDTO);
//
//        assertNull(result);
//        assertEquals("Rejected", form.getHeadStatus());
//        assertEquals(approvalDTO.getUser(), form.getHead());
//        assertEquals(approvalDTO.getDescription(), form.getHeadDescription());
//        verify(accidentLeaveFormRepo).save(form);
//    }

//    @Test
//    void testAcceptForm_UserNotHeadOfDepartment() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//
//        AccidentLeaveForm accidentLeaveForm = new AccidentLeaveForm();
//        User user = new User();
//        user.setJob_type("Lecturer");
//
//        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(accidentLeaveForm));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//
//        Object result = accidentLeaveFormService.acceptForm(formId, approvalDTO);
//
//        // In this case, no status change is expected since the user is not the Head of the Department
//        assertEquals("Failed", result);
//
//        verify(accidentLeaveFormRepo).findById(formId);
//        verify(userRepo).findById(approvalDTO.getUser());
//        verify(accidentLeaveFormRepo, never()).save(any(AccidentLeaveForm.class)); // Save should not be called
//    }

//    @Test
//    void testRejectForm_Dean() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//        approvalDTO.setDescription("Rejection by Dean");
//
//        AccidentLeaveForm form = new AccidentLeaveForm();
//        User user = new User();
//        user.setJob_type("Dean");
//
//        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(form));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//        when(accidentLeaveFormRepo.save(any(AccidentLeaveForm.class))).thenReturn(form);
//
//        Object result = accidentLeaveFormService.rejectForm(formId, approvalDTO);
//
//        assertNotNull(result);
//        assertEquals("Rejected", form.getDeanStatus());
//        assertEquals(approvalDTO.getUser(), form.getDean());
//        assertEquals(approvalDTO.getDescription(), form.getDeanDescription());
//        verify(accidentLeaveFormRepo).save(form);
//    }
//    @Test
//    void testRejectForm_UserNotDean() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//        approvalDTO.setDescription("Rejection by Dean");
//
//        AccidentLeaveForm form = new AccidentLeaveForm();
//        User user = new User();
//        user.setJob_type("Lecturer");
//
//        when(accidentLeaveFormRepo.findById(formId)).thenReturn(Optional.of(form));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//
//        Object result = accidentLeaveFormService.rejectForm(formId, approvalDTO);
//
//        assertEquals("Failed", result);
//
//        verify(accidentLeaveFormRepo).findById(formId);
//        verify(userRepo).findById(approvalDTO.getUser());
//        verify(accidentLeaveFormRepo, never()).save(any(AccidentLeaveForm.class)); // Save should not be called
//    }

}