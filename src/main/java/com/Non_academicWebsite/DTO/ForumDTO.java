package com.Non_academicWebsite.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForumDTO {
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
    private String subject;
    @Lob
    @Column(length = 512000)
    private String body;
}
