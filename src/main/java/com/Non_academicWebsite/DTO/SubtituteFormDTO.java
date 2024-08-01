package com.Non_academicWebsite.DTO;

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
public class SubtituteFormDTO {
    private String name;
    private String emp_id;
    private String faculty;
    private String department;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date start_date;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date end_date;
    private String acting;
    private String reason;
}
