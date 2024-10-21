package com.Non_academicWebsite.DTO.Forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccidentLeaveFormDTO {
    private String accidentOccurredDuring;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date dateAndTimeOfAccident;
    private String placeOFAccident;
    private String whilePerformingAnyDuty;
    private String natureOfDanger;
    private String whoInspectTheAccident;
    private String whoInformedAfterAccident;
    private String referralForTreatment;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date dateAndTimeOfReport;
    private String durationOfHospitalStay;
    private String isPoliceComplaint;
    private String expectAccidentCompensation;
    private byte[] file;
    private String fileType;
    private String fileName;
    private String status;
    private Date createdAt;
}
