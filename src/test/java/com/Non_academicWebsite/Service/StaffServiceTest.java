package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Config.JwtService;
import com.Non_academicWebsite.DTO.RegisterDTO;
import com.Non_academicWebsite.DTO.SecurityDTO;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ForumRepo;
import com.Non_academicWebsite.Repository.RegisterConfirmationTokenRepo;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Response.UserInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class StaffServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RegisterConfirmationTokenRepo registerConfirmationTokenRepo;
    @Mock
    private ForumRepo forumRepo;
    @InjectMocks
    private StaffService staffService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_GetUser_WithValidValues() {
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .first_name("John")
                .last_name("Doe")
                .date_of_birth(new Date())
                .gender("male")
                .email("user@gmail.com")
                .phone_no(1234567890L)
                .password("password")
                .address("123 Main St")
                .city("Jaffna")
                .postal_code(12345)
                .ic_no("1234567890ABC")
                .emp_id("EMP001")
                .job_type("developer")
                .department("IT")
                .faculty("Engineering")
                .role(Role.USER)
                .app_password("31243321424")
                .image_type( null)
                .image_name(null)
                .image_data(null)
                .createdAt(new Date())
                .updatedAt(new Date())
                .verified(false)
                .build();

        String token = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";

        when(jwtService.extractUserEmail(token)).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        UserInfoResponse userInfoResponse = staffService.getUser(token);

        assertEquals(userInfoResponse.getId(), mockUser.getId());
        assertEquals(userInfoResponse.getEmp_id(), mockUser.getEmp_id());
        assertEquals(userInfoResponse.getFirst_name(), mockUser.getFirst_name());
        assertEquals(userInfoResponse.getLast_name(), mockUser.getLast_name());
        assertEquals(userInfoResponse.getDate_of_birth(), mockUser.getDate_of_birth());
        assertEquals(userInfoResponse.getGender(), mockUser.getGender());
        assertEquals(userInfoResponse.getEmail(), mockUser.getEmail());
        assertEquals(userInfoResponse.getPhone_no(), mockUser.getPhone_no());
        assertEquals(userInfoResponse.getAddress(), mockUser.getAddress());
        assertEquals(userInfoResponse.getCity(), mockUser.getCity());
        assertEquals(userInfoResponse.getPostal_code(), mockUser.getPostal_code());
        assertEquals(userInfoResponse.getIc_no(), mockUser.getIc_no());
        assertEquals(userInfoResponse.getJob_type(), mockUser.getJob_type());
        assertEquals(userInfoResponse.getDepartment(), mockUser.getDepartment());
        assertEquals(userInfoResponse.getFaculty(), mockUser.getFaculty());
        assertEquals(userInfoResponse.getImage_type(), mockUser.getImage_type());
        assertEquals(userInfoResponse.getImage_name(), mockUser.getImage_name());
        assertNull(userInfoResponse.getImage_data());
        assertEquals(userInfoResponse.getRole(), mockUser.getRole().toString());
        assertEquals(userInfoResponse.getCreatedAt(), mockUser.getCreatedAt());
        assertEquals(userInfoResponse.getUpdatedAt(), mockUser.getUpdatedAt());

        assertNotNull(userInfoResponse);

    }

    @Test
    void Test_GetUsers_WithNullEmptyValues() {
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .first_name("")
                .last_name(null)
                .date_of_birth(null)
                .gender("")
                .email("user@gmail.com")
                .phone_no(null)
                .password("password")
                .address("")
                .city("")
                .postal_code(null)
                .ic_no("")
                .emp_id("EMP001")
                .job_type("developer")
                .department("IT")
                .faculty("Engineering")
                .role(null)
                .app_password("")
                .image_type( null)
                .image_name(null)
                .image_data(null)
                .createdAt(new Date())
                .updatedAt(new Date())
                .verified(false)
                .build();

        String token = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";

        when(jwtService.extractUserEmail(token)).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        UserInfoResponse userInfoResponse = staffService.getUser(token);

        assertEquals(userInfoResponse.getId(), mockUser.getId());
        assertEquals(userInfoResponse.getEmp_id(), mockUser.getEmp_id());
        assertEquals(userInfoResponse.getFirst_name(), mockUser.getFirst_name());
        assertEquals(userInfoResponse.getLast_name(), mockUser.getLast_name());
        assertEquals(userInfoResponse.getDate_of_birth(), mockUser.getDate_of_birth());
        assertEquals(userInfoResponse.getGender(), mockUser.getGender());
        assertEquals(userInfoResponse.getEmail(), mockUser.getEmail());
        assertEquals(userInfoResponse.getPhone_no(), mockUser.getPhone_no());
        assertEquals(userInfoResponse.getAddress(), mockUser.getAddress());
        assertEquals(userInfoResponse.getCity(), mockUser.getCity());
        assertEquals(userInfoResponse.getPostal_code(), mockUser.getPostal_code());
        assertEquals(userInfoResponse.getIc_no(), mockUser.getIc_no());
        assertEquals(userInfoResponse.getJob_type(), mockUser.getJob_type());
        assertEquals(userInfoResponse.getDepartment(), mockUser.getDepartment());
        assertEquals(userInfoResponse.getFaculty(), mockUser.getFaculty());
        assertEquals(userInfoResponse.getImage_type(), mockUser.getImage_type());
        assertEquals(userInfoResponse.getImage_name(), mockUser.getImage_name());
        assertNull(userInfoResponse.getImage_data());
        assertEquals(userInfoResponse.getRole(), "none");
        assertEquals(userInfoResponse.getCreatedAt(), mockUser.getCreatedAt());
        assertEquals(userInfoResponse.getUpdatedAt(), mockUser.getUpdatedAt());

        assertNotNull(userInfoResponse);

    }

    @Test
    void test_getUser_with_nullUser() {
        String token = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> staffService.getUser(token));
    }

    @Test
    void test_updateProfile_withValidValues() throws IOException {
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .first_name("John")
                .last_name("Doe")
                .date_of_birth(new Date())
                .gender("male")
                .email("user@gmail.com")
                .phone_no(1234567890L)
                .password("password")
                .address("123 Main St")
                .city("Jaffna")
                .postal_code(12345)
                .ic_no("1234567890ABC")
                .emp_id("EMP001")
                .job_type("developer")
                .department("IT")
                .faculty("Engineering")
                .role(Role.USER)
                .app_password("31243321424")
                .image_type( null)
                .image_name(null)
                .image_data(null)
                .createdAt(new Date())
                .updatedAt(new Date())
                .verified(false)
                .build();

        String token = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirst_name("James");
        registerDTO.setLast_name("Bond");
        registerDTO.setDate_of_birth(new Date());
        registerDTO.setGender("male");
        registerDTO.setPhone_no(760832397L);
        registerDTO.setAddress("Jaffna");
        registerDTO.setCity("Jaffna");
        registerDTO.setPostal_code(20000);
        registerDTO.setIc_no("6923421314v");

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        User user = staffService.updateProfile(token, registerDTO, null);

        assertEquals(user.getFirst_name(), registerDTO.getFirst_name());
        assertEquals(user.getLast_name(), registerDTO.getLast_name());
        assertEquals(user.getDate_of_birth(), registerDTO.getDate_of_birth());
        assertEquals(user.getGender(), registerDTO.getGender());
        assertEquals(user.getPhone_no(), registerDTO.getPhone_no());
        assertEquals(user.getAddress(), registerDTO.getAddress());
        assertEquals(user.getCity(), registerDTO.getCity());
        assertEquals(user.getPostal_code(), registerDTO.getPostal_code());
        assertEquals(user.getIc_no(), registerDTO.getIc_no());

    }

    @Test
    void test_resetPassword_SameNewPassword(){
        String header = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .email("user@gmail.com")
                .password("password")
                .build();

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        SecurityDTO updatePasswordDTO = new SecurityDTO();
        updatePasswordDTO.setOld_password("password");
        updatePasswordDTO.setNew_password("password");
        updatePasswordDTO.setPassword_for_delete("password");

        when(passwordEncoder.matches(updatePasswordDTO.getOld_password(), mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(updatePasswordDTO.getNew_password(), mockUser.getPassword())).thenReturn(true);

        assertEquals(staffService.resetPassword(header, updatePasswordDTO), "Can't be same password");

    }

    @Test
    void test_resetPassword_withWrong_oldPassword(){
        String header = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .email("user@gmail.com")
                .password("password")
                .build();

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        SecurityDTO updatePasswordDTO = new SecurityDTO();
        updatePasswordDTO.setOld_password("password00");
        updatePasswordDTO.setNew_password("svsddsgf");
        updatePasswordDTO.setPassword_for_delete("faefasefsf");

        when(passwordEncoder.matches(updatePasswordDTO.getOld_password(), mockUser.getPassword())).thenReturn(false);

        assertEquals(staffService.resetPassword(header, updatePasswordDTO), "Reset rejected");

    }

    @Test
    void test_resetPassword_withValidValues(){
        String header = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .email("user@gmail.com")
                .password("password")
                .build();

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        SecurityDTO updatePasswordDTO = new SecurityDTO();
        updatePasswordDTO.setOld_password("password");
        updatePasswordDTO.setNew_password("newPassword");

        when(passwordEncoder.matches(updatePasswordDTO.getOld_password(), mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(updatePasswordDTO.getNew_password(), mockUser.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(userRepo.save(mockUser)).thenReturn(mockUser);

        String string = staffService.resetPassword(header, updatePasswordDTO);
        assertEquals(string, "Reset success");
        verify(userRepo, times(1)).save(mockUser);

    }

    @Test
    void test_deleteAccount_withWrong_password() {
        String header = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .email("user@gmail.com")
                .password("password")
                .build();

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        SecurityDTO deleteAccountDTO = new SecurityDTO();
        deleteAccountDTO.setPassword_for_delete("WrongPassword");

        when(passwordEncoder.matches(deleteAccountDTO.getPassword_for_delete(), mockUser.getPassword())).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> staffService.deleteAccount(header, deleteAccountDTO));

    }

    @Test
    void test_deleteAccount_withValidPassword() {
        String header = "Bearer safaefafaassa.asasfasfasfasf.afasfasfasfaaegf";
        User mockUser = User.builder()
                .id("EN_CO_MN_005")
                .email("user@gmail.com")
                .build();

        when(jwtService.extractUserEmail(anyString())).thenReturn("user@gmail.com");
        when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(mockUser));

        SecurityDTO deleteAccountDTO = new SecurityDTO();
        deleteAccountDTO.setPassword_for_delete("password");

        when(passwordEncoder.matches(deleteAccountDTO.getPassword_for_delete(), mockUser.getPassword())).thenReturn(true);

        String string = staffService.deleteAccount(header, deleteAccountDTO);
        assertEquals(string, "delete success");

        verify(forumRepo, times(1)).deleteByUserId(mockUser.getId());
        verify(registerConfirmationTokenRepo, times(1)).deleteByUserId(mockUser.getId());
        verify(userRepo, times(1)).delete(mockUser);

    }
}