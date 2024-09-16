package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AccidentLeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String formType;
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
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
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
