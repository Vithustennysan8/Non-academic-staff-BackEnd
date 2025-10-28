package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class NormalLeaveForm implements Forms{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formType;
    private String upfNo;
    private String designation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate firstAppointmentDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate leaveAt;
    private int leaveDays;
    private String reason;
    private String arrangement;
    private String addressDuringTheLeave;
    private String head;
    private String headStatus;
    private String headDescription;
    private LocalDateTime headReactedAt;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;

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
    public LocalDate getLeaveAt(){
        return leaveAt;
    }

    @Override
    public int getLeaveDays(){
        return leaveDays;
    }
}
