package com.Non_academicWebsite.Entity;

import jakarta.persistence.*;
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
@Entity
public class FullLeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String emp_id;
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
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file_data;
    private String file_name;
    private String file_type;

}
