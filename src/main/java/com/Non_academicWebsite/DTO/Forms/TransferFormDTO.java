package com.Non_academicWebsite.DTO.Forms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferFormDTO {
    private String designation;
    private String servicePeriodOfCurrent;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate currentJobStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate currentJobEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate previousJobStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate previousJobEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveAt;
    private int leaveDays;

}
