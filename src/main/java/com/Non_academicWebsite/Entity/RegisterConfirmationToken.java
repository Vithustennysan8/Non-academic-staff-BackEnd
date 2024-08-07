package com.Non_academicWebsite.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RegisterConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date expiresAt;
    private Date confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
