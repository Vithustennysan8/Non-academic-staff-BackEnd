package com.Non_academicWebsite.Entity.Forms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fieldName;
    private String fieldType;
    private String fieldPlaceholder;
    private boolean isRequired;
    @ManyToOne
    @JoinColumn(name = "dynamic_form_id", nullable = false)
    private DynamicForm dynamicForm;
    private int sequence;
}
