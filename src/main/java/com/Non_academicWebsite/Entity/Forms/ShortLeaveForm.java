package com.Non_academicWebsite.Entity.Forms;

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
public class ShortLeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String emp_id;
    private String userId;
    private String faculty;
    private String department;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date job_start_date;
    private int duration;
    private String leave_type;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date start_date;
    private String reason;
    private boolean status;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file_data;
    private String file_name;
    private String file_type;
}
