package com.Non_academicWebsite.DTO.Forms;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NormalLeaveFormDTO {
    private String upfNo;
    private String designation;
    private Date firstAppointmentDate;
    private Integer casualLeaveLastYear;
    private Integer vacationLeaveLastYear;
    private Integer sickLeaveLastYear;
    private Integer casualLeaveThisYear;
    private Integer vacationLeaveThisYear;
    private Integer sickLeaveThisYear;
    private Integer noOfLeaveDays;
    private String leaveType;
    private Date leaveAppliedDate;
    @Lob
    @Column(length = 512000)
    private String reason;
    private String arrangement;
    private String addressDuringTheLeave;
    private String orderOfHead;
}
