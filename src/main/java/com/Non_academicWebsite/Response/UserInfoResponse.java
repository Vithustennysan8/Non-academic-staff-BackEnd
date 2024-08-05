package com.Non_academicWebsite.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String id;
    private String first_name;
    private String last_name;
    private Date date_of_birth;
    private String gender;
    private String email;
    private Long phone_no;
    private String address;
    private String city;
    private Integer postal_code;
    private String ic_no;
    private String emp_id;
    private String job_type;
    private String department;
    private String faculty;
    private Date createdAt;
    private Date updatedAt;
    private String image_name;
    private String image_type;
    private String image_data;
    private boolean isLogin;
}
