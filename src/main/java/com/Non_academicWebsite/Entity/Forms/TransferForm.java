package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransferForm implements Forms{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formType;
    private String designation;
    private String servicePeriodOfCurrent;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;
    private String fileName;
    private String fileType;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate currentJobStartDate;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate currentJobEndDate;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate previousJobStartDate;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate previousJobEndDate;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate leaveAt;
    private int leaveDays;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime updatedAt;
    private String head;
    private String headStatus;
    private String headDescription;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime headReactedAt;
    private String dean;
    private String deanStatus;
    private String deanDescription;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime deanReactedAt;
    private String registrar;
    private String registrarStatus;
    private String registrarDescription;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrarReactedAt;
    private String nae;
    private String naeStatus;
    private String naeDescription;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime naeReactedAt;
    private String registrarApproval;
    private String registrarApprovalStatus;
    private String registrarApprovalDescription;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrarApprovalAt;
    private String status;

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
