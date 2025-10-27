package com.Non_academicWebsite.Service.Forms;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.ApprovalDTO;
import com.Non_academicWebsite.DTO.Forms.NormalLeaveFormDTO;
import com.Non_academicWebsite.DTO.ReqFormsDTO;
import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.Forms.NormalLeaveFormRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class NormalLeaveFormServiceTest {

    @InjectMocks
    private NormalLeaveFormService normalLeaveFormService;
    @Mock
    private NormalLeaveFormRepo normalLeaveFormRepo;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNormalLeaveForms_UserNotFound() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";

        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        Object result = normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, token);

        assertEquals(Collections.emptyList(), result);
        verify(jwtService).extractUserEmail(token.substring(7));
        verify(userRepo).findByEmail(email);
    }

    @Test
    void testGetNormalLeaveForms_NoDepartmentNoFaculty() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setId("testUserId12345"); // prefix = "testUserId"
        user.setEmail(email);

        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByUserIdStartingWith("testUserId")).thenReturn(Collections.singletonList(new NormalLeaveForm()));

        Object result = normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, token);

        assertNotNull(result);
        assertInstanceOf(List.class, result);
        verify(normalLeaveFormRepo, times(0)).findByUserIdStartingWith("testUserId");
    }

    @Test
    void testGetNormalLeaveForms_FacultyNull() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";

        User user = new User();
        user.setId("testUserId12345");
        user.setEmail(email);
        user.setFaculty("Engineering");

        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setDepartment("Computer Science");

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByDepartment(user.getFaculty(), "Computer Science"))
                .thenReturn(Collections.singletonList(new NormalLeaveForm()));

        Object result = normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, token);

        assertNotNull(result);
        assertInstanceOf(List.class, result);
        verify(normalLeaveFormRepo, times(1)).findByDepartment("Engineering", "Computer Science");
    }

    @Test
    void testGetNormalLeaveForms_DepartmentNull() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setId("testUserId12345");
        user.setEmail(email);

        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setFaculty("Engineering");

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByFaculty("Engineering")).thenReturn(Collections.singletonList(new NormalLeaveForm()));

        Object result = normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, token);

        assertNotNull(result);
        assertInstanceOf(List.class, result);
        verify(normalLeaveFormRepo,times(1)).findByFaculty("Engineering");
    }

    @Test
    void testGetNormalLeaveForms_BothFacultyAndDepartmentPresent() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setId("testUserId12345");
        user.setEmail(email);

        ReqFormsDTO reqFormsDTO = new ReqFormsDTO();
        reqFormsDTO.setFaculty("Engineering");
        reqFormsDTO.setDepartment("Computer Science");

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByFacultyAndDepartment("Engineering", "Computer Science"))
                .thenReturn(Collections.singletonList(new NormalLeaveForm()));

        Object result = normalLeaveFormService.getNormalLeaveForms(reqFormsDTO, token);

        assertNotNull(result);
        assertInstanceOf(List.class, result);
        verify(normalLeaveFormRepo).findByFacultyAndDepartment("Engineering", "Computer Science");
    }

    @Test
    void testGetForms_UserNotFound() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        List<NormalLeaveForm> result = normalLeaveFormService.getForms(token);

        assertEquals(Collections.emptyList(), result);
        verify(jwtService).extractUserEmail(token.substring(7));
        verify(userRepo).findByEmail(email);
    }

    @Test
    void testGetForms_ValidUser() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setId("testUserId12345");  // Prefix = "testUserId"
        user.setEmail(email);

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByUserIdStartingWith("testUser"))
                .thenReturn(Collections.singletonList(new NormalLeaveForm()));

        List<NormalLeaveForm> result = normalLeaveFormService.getForms(token);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jwtService).extractUserEmail(token.substring(7));
        verify(userRepo).findByEmail(email);
        verify(normalLeaveFormRepo).findByUserIdStartingWith("testUser");
    }

    @Test
    void testGetForms_EmptyLeaveForms() throws ResourceNotFoundException {
        String token = "Bearer some.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setId("testUserId12345"); // Prefix = "testUserId"
        user.setEmail(email);

        when(jwtService.extractUserEmail(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(normalLeaveFormRepo.findByUserIdStartingWith("testUser"))
                .thenReturn(Collections.emptyList());

        List<NormalLeaveForm> result = normalLeaveFormService.getForms(token);

        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);
        verify(jwtService).extractUserEmail(token.substring(7));
        verify(userRepo).findByEmail(email);
        verify(normalLeaveFormRepo).findByUserIdStartingWith("testUser");
    }

    @Test
    void testAcceptForm_NormalLeaveFormNotFound() {
        Long formId = 1L;
        ApprovalDTO approvalDTO = new ApprovalDTO();
        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID

        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.empty());

        Object result = normalLeaveFormService.acceptForm(formId, approvalDTO);

        assertEquals("Failed", result);
        verify(normalLeaveFormRepo).findById(formId);
    }

    @Test
    void testAcceptForm_UserNotFound() {
        Long formId = 1L;
        ApprovalDTO approvalDTO = new ApprovalDTO();
        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID

        NormalLeaveForm normalLeaveForm = new NormalLeaveForm();
        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.of(normalLeaveForm));
        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            normalLeaveFormService.acceptForm(formId, approvalDTO);
        });

        verify(normalLeaveFormRepo).findById(formId);
        verify(userRepo).findById(approvalDTO.getUser());
    }

//

//    @Test
//    void testAcceptForm_UserNotHeadOfDepartment() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//
//        NormalLeaveForm normalLeaveForm = new NormalLeaveForm();
//        User user = new User();
//        user.setJob_type("Lecturer");
//
//        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.of(normalLeaveForm));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//
//        Object result = normalLeaveFormService.acceptForm(formId, approvalDTO);
//
//        // In this case, no status change is expected since the user is not the Head of the Department
//        assertEquals("Failed", result);
//
//        verify(normalLeaveFormRepo).findById(formId);
//        verify(userRepo).findById(approvalDTO.getUser());
//        verify(normalLeaveFormRepo, never()).save(any(NormalLeaveForm.class)); // Save should not be called
//    }

    @Test
    void testRejectForm_NormalLeaveFormNotFound() {
        Long formId = 1L;
        ApprovalDTO approvalDTO = new ApprovalDTO();
        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID

        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.empty());

        Object result = normalLeaveFormService.rejectForm(formId, approvalDTO);

        assertEquals("Failed", result);
        verify(normalLeaveFormRepo).findById(formId);
    }

    @Test
    void testRejectForm_UserNotFound() {
        Long formId = 1L;
        ApprovalDTO approvalDTO = new ApprovalDTO();
        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID

        NormalLeaveForm normalLeaveForm = new NormalLeaveForm();
        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.of(normalLeaveForm));
        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            normalLeaveFormService.rejectForm(formId, approvalDTO);
        });

        verify(normalLeaveFormRepo).findById(formId);
        verify(userRepo).findById(approvalDTO.getUser());
    }

//    @Test
//    void testRejectForm_UserIsHeadOfDepartment() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//        approvalDTO.setDescription("Rejecting the leave");
//
//        NormalLeaveForm normalLeaveForm = new NormalLeaveForm();
//        User user = new User();
//        user.setJob_type("Head of the Department");
//
//        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.of(normalLeaveForm));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//        when(normalLeaveFormRepo.save(any(NormalLeaveForm.class))).thenReturn(normalLeaveForm);
//
//        Object result = normalLeaveFormService.rejectForm(formId, approvalDTO);
//
//        assertNotNull(result);
//        assertEquals(normalLeaveForm, result);
//        assertEquals("Rejected", normalLeaveForm.getHeadStatus());
//        assertEquals(approvalDTO.getUser(), normalLeaveForm.getHead());
//        assertEquals(approvalDTO.getDescription(), normalLeaveForm.getHeadDescription());
//        assertNotNull(normalLeaveForm.getHeadReactedAt());
//        assertEquals("Rejected", normalLeaveForm.getStatus());
//
//        verify(normalLeaveFormRepo).findById(formId);
//        verify(userRepo).findById(approvalDTO.getUser());
//        verify(normalLeaveFormRepo).save(normalLeaveForm);
//    }

//    @Test
//    void testRejectForm_UserNotHeadOfDepartment() {
//        Long formId = 1L;
//        ApprovalDTO approvalDTO = new ApprovalDTO();
//        approvalDTO.setUser("testUserId12345"); // Arbitrary user ID
//
//        NormalLeaveForm normalLeaveForm = new NormalLeaveForm();
//        User user = new User();
//        user.setJob_type("Lecturer");
//
//        when(normalLeaveFormRepo.findById(formId)).thenReturn(Optional.of(normalLeaveForm));
//        when(userRepo.findById(approvalDTO.getUser())).thenReturn(Optional.of(user));
//
//        Object result = normalLeaveFormService.rejectForm(formId, approvalDTO);
//
//        // In this case, no status change is expected since the user is not the Head of the Department
//        assertEquals("Failed", result);
//        assertNull(normalLeaveForm.getHeadStatus());
//        assertNull(normalLeaveForm.getHead());
//        assertNull(normalLeaveForm.getHeadDescription());
//        assertNull(normalLeaveForm.getHeadReactedAt());
//        assertNull(normalLeaveForm.getStatus());
//
//        verify(normalLeaveFormRepo).findById(formId);
//        verify(userRepo).findById(approvalDTO.getUser());
//        verify(normalLeaveFormRepo, never()).save(any(NormalLeaveForm.class)); // Save should not be called
//    }

    @Test
    void rejectForm() {
    }
}