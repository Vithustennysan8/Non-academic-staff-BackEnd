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
    private Long id;
    private Long formId;  // dynamicFormUser id
    private String formFlowType;
    private String approver;
    private String approverId;
    private int approverOrder;
    private String approverStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date approvalAt;
    private String approvalDescription;
}
