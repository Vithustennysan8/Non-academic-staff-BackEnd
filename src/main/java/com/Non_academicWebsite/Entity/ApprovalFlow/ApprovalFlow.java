package com.Non_academicWebsite.Entity.ApprovalFlow;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"dynamic_form_id", "unique_name", "department", "faculty", "sequence", "role_name"}
))
public class ApprovalFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dynamicForm;
    @Column(nullable = false)
    private String uniqueName;
    @Column(nullable = false)
    private String roleName;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private String faculty;
    @Column(nullable = false)
    private String department;
    private String updatedBy;
    private Date updatedAt;
}
