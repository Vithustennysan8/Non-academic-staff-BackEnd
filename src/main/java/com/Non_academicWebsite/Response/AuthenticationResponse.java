package com.Non_academicWebsite.Response;

import com.Non_academicWebsite.Entity.User;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private User user;
    private String message;
}
