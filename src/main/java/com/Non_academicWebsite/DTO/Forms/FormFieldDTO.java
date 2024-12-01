package com.Non_academicWebsite.DTO.Forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormFieldDTO {
    private String label;
    private String type;
    private String placeholder;
    private boolean required;
    private String value;
    private List<String> options;
}
