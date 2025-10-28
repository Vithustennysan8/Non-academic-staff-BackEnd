package com.Non_academicWebsite.Entity.ApprovalFlow;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
