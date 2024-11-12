package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.OtpConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OtpConfirmationRepo extends JpaRepository<OtpConfirmationToken, Integer> {
    OtpConfirmationToken findByOtp(Integer otp);
}
