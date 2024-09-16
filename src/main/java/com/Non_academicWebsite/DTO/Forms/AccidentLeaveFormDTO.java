package com.Non_academicWebsite.DTO.Forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccidentLeaveFormDTO {
    private String accidentOccurredDuring;
    private Date DateAndTimeOfAccident;
    private String placeOFAccident;
    private String whilePerformingAnyDuty;
    private String natureOfDanger;
    private String whoInspectTheAccident;
    private String whoInformedAfterAccident;
    private String referralForTreatment;
    private Date dateAndTimeOfReport;
    private String durationOfHospitalStay;
    private String isPoliceComplaint;
    private String expectAccidentCompensation;
    private byte[] file;
    private String fileType;
    private String fileName;
    private String approverOne;
    private String approverOneStatus;
    private String approverOneDescription;
    private Date approverOneReactedAt;
    private String approverTwo;
    private String approverTwoStatus;
    private String approverTwoDescription;
    private Date approverTwoReactedAt;
    private String status;
    private Date createdAt;
}
