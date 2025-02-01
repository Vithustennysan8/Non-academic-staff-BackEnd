package com.Non_academicWebsite.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalDTO {
    private String user;
    private String description;
    private String formType;
    private Long formId;
}
