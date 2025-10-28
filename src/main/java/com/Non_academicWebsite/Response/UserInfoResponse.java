package com.Non_academicWebsite.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String id;
    private String first_name;
    private String last_name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;
    private String gender;
    private String email;
    private String normalEmail;
    private Long phone_no;
    private String address;
    private String city;
    private Integer postal_code;
    private String ic_no;
    private String emp_id;
    private String job_type;
    private String department;
    private String faculty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String image_name;
    private String image_type;
    private String image_data;
    private boolean isLogin;
    private String role;
}
