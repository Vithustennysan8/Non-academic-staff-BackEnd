package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class NormalLeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String formType;
    private String upfNo;
    private String designation;
    private Date firstAppointmentDate;
    private Integer casualLeaveLastYear;
    private Integer vacationLeaveLastYear;
    private Integer sickLeaveLastYear;
    private Integer casualLeaveThisYear;
    private Integer vacationLeaveThisYear;
    private Integer sickLeaveThisYear;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Integer noOfLeaveDays;
    private String leaveType;
    private Date leaveAppliedDate;
    @Lob
    @Column(length = 512000)
    private String reason;
    private String arrangement;
    private String addressDuringTheLeave;
    private String approverOne;
    private String approverOneStatus;
    private String approverOneDescription;
    private Date approverOneReactedAt;
    private String status;

}
