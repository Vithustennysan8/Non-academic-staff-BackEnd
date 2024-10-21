package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class MedicalLeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formType;
    private String designation;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date requestPeriodStart;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date requestPeriodEnd;
    private String fileName;
    private String fileType;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Date createdAt;
    private Date updatedAt;
    private String head;
    private String headStatus;
    private String headDescription;
    private Date headReactedAt;
    private String dean;
    private String deanStatus;
    private String deanDescription;
    private Date deanReactedAt;
    private String cmo;
    private String cmoStatus;
    private String cmoDescription;
    private Date cmoReactedAt;
    private String registrar;
    private String registrarStatus;
    private String registrarDescription;
    private Date registrarReactedAt;
    private String nae;
    private String naeStatus;
    private String naeDescription;
    private Date naeReactedAt;
    private String status;
}
