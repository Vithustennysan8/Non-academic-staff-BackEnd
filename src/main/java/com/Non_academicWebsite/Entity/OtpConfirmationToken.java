package com.Non_academicWebsite.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OtpConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date updatedAt;
    private Date confirmedAt;
    @Column(nullable = false)
    private Integer otp;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
