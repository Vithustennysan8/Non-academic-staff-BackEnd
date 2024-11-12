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
public class TransferFormDTO {
    private String designation;
    private String servicePeriodOfCurrent;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date currentJobStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date currentJobEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date previousJobStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date previousJobEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaveAt;
    private int leaveDays;

}
