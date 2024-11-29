package com.Non_academicWebsite.Entity.ApprovalFlow;

import com.Non_academicWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String formType;
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
