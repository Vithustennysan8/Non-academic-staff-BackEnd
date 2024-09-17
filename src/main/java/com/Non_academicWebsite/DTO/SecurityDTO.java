package com.Non_academicWebsite.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityDTO {
    private String new_password;
    private String old_password;
    private String password_for_delete;
}
