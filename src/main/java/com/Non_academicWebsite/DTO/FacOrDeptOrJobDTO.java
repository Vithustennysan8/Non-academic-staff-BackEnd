package com.Non_academicWebsite.DTO;

import com.Non_academicWebsite.Entity.JobScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacOrDeptOrJobDTO {
    private String faculty;
    private String name;
    private JobScope scope;
    private String alias;
}
