package com.Non_academicWebsite.Entity.ApprovalFlow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormApprover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long formId;  // dynamic form id
    public String formFlowType;
    public String approver;
    public String approverId;
    public int approverOrder;
    public String approverStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date approvalAt;
    public String approvalDescription;
}
