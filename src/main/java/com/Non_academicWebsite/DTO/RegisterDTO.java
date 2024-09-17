package com.Non_academicWebsite.DTO;

import com.Non_academicWebsite.Entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterDTO {
    private String first_name;
    private String last_name;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date date_of_birth;
    private String gender;
    private String email;
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
    private String faculty;
    private Role role;
}
