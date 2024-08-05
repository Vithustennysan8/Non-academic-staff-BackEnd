package com.Non_academicWebsite.DTO.Forms;

import jakarta.persistence.Column;
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
public class FullLeaveFormDTO {
    private String name;
    private String empId;
    private String faculty;
    private String department;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date job_start_date;
    private int leave_days;
    private String leave_type;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date start_date;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date end_date;
    private String acting;
    private String reason;

}
