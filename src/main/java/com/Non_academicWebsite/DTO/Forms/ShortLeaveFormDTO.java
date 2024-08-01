package com.Non_academicWebsite.DTO.Forms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
public class ShortLeaveFormDTO {
    private Long id;
    private String name;
    private String emp_id;
    private String faculty;
    private String department;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date job_start_date;
    private int duration;
    private String leave_type;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date leave_date;
    private String reason;
}
