package com.Non_academicWebsite.DTO.Forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaternalLeaveFormDTO {
    private String designation;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date childBirthDate;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date requestedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaveAt;
    private int leaveDays;
}
