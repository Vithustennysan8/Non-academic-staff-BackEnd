//package com.Non_academicWebsite.Controller;
//
//import com.Non_academicWebsite.DTO.RegisterDTO;
//import com.Non_academicWebsite.Entity.Role;
//import com.Non_academicWebsite.Entity.User;
//import com.Non_academicWebsite.Service.AuthenticationService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Date;
//
//import static org.awaitility.Awaitility.given;
//import static org.junit.jupiter.api.Assertions.*;
//
//@WebMvcTest(AuthenticationController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//class AuthenticationControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void test_register_withValidValues() throws Exception {
//        RegisterDTO registerDTO = RegisterDTO.builder()
//                .first_name("John")
//                .last_name("Doe")
//                .date_of_birth(new Date())
//                .gender("male")
//                .email("user@gmail.com")
//                .phone_no(1234567890L)
//                .password("password")
//                .address("123 Main St")
//                .city("Jaffna")
//                .postal_code(12345)
//                .ic_no("1234567890ABC")
//                .emp_id("EMP001")
//                .job_type("developer")
//                .department("IT")
//                .faculty("Engineering")
//                .role(Role.USER)
//                .app_password("31243321424")
//                .build();
//
//        given(authenticationService.registerStaff(ArgumentMatchers.any(RegisterDTO.class), ArgumentMatchers.any()).;
//
//    }
//
//    @Test
//    void login() {
//    }
//}