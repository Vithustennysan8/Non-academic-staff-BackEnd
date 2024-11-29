package com.Non_academicWebsite.DTO.ApprovalFlow;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalFlowDTO {
    private String formType;
    private List<ApprovalStage> approvalStage;

    @Data
    public static class ApprovalStage{
        private String roleName;
        private int sequence;
    }
}
