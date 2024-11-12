package com.Non_academicWebsite.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordDTO {
    private String email;
    private Integer otp;
    private String newPassword;
}
