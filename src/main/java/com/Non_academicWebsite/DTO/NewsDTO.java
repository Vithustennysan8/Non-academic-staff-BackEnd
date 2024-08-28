package com.Non_academicWebsite.DTO;

import com.Non_academicWebsite.Entity.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDTO {
    private String heading;
    private String body;
}

