package com.Non_academicWebsite.DTO;

import com.Non_academicWebsite.Entity.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterDTO {
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String gender;
    private String email;
    private String normalEmail;
    private String app_password;
    private Long phone_no;
    private String password;
    private String address;
    private String city;
    private Integer postal_code;
    private String ic_no;
    private String emp_id;
    private String job_type;
    private String department;
    private Integer facultyId;
    private String faculty;
    private Role role;
}
