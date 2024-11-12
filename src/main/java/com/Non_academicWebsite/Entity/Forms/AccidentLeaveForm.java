package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
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
public class AccidentLeaveForm implements Forms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formType;
    private String accidentOccurredDuring;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date dateAndTimeOfAccident;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaveAt;
    private int leaveDays;
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
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;
    private String fileType;
    private String fileName;
    private String head;
    private String headStatus;
    private String headDescription;
    private Date headReactedAt;
    private String dean;
    private String deanStatus;
    private String deanDescription;
    private Date deanReactedAt;
    private String cmo;
    private String cmoStatus;
    private String cmoDescription;
    private Date cmoReactedAt;
    private String nae;
    private String naeStatus;
    private String naeDescription;
    private Date naeReactedAt;
    private String status;
    private Date createdAt;

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getFormType() {
        return formType;
    }
    @Override
    public Date getLeaveAt(){
        return leaveAt;
    }

    @Override
    public int getLeaveDays(){
        return leaveDays;
    }
}
