package com.Non_academicWebsite.Entity.ApprovalFlow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormApprover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long formId;
    public String formFlowType;
    public String approver;
    public String approverId;
    public int approverOrder;
    public String approverStatus;
    public String approvalAt;
    public String approvalDescription;
}
