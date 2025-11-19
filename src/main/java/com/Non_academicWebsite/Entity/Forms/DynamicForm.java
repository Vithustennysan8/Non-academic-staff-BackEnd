package com.Non_academicWebsite.Entity.Forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DynamicForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String formType;
    private String department;
    private String faculty;
    private boolean isAvailable = true;

    @OneToMany(mappedBy = "dynamicForm", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FormField> formFields;
}
