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
public class TransferForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String emp_id;
    private String faculty;
    private String department;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date job_start_date;
    private String experience;
    private String preference1;
    private String preference2;
    private String preference3;
    private String reason;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file_data;
    private String file_name;
    private String file_type;
}
