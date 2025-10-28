package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Entity.OtpConfirmationToken;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.OtpConfirmationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OtpConfirmationService {

    private final OtpConfirmationRepo otpConfirmationRepo;
    private final MailService mailService;

    public void sendOtp(User user, String email) {
        Integer otp = generateOtp();
        OtpConfirmationToken otpToken = OtpConfirmationToken.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .otp(otp)
                .updatedAt(LocalDateTime.now())
                .build();
        otpConfirmationRepo.save(otpToken);
        // Send OTP to email
        mailService.sendMailForOTP(otp, user.getFirst_name(), email);
    }

    private Integer generateOtp() {
        return (int) (Math.random() * 1000000);
    }

    public String confirmOtp(Integer otp) {
        OtpConfirmationToken otpToken = otpConfirmationRepo.findByOtp(otp);
        if (otpToken == null || otpToken.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            return "Invalid OTP or OTP expired";
        }
        otpToken.setConfirmedAt(LocalDateTime.now());
        otpConfirmationRepo.save(otpToken);
        return "OTP confirmed successfully";
    }

    public String getUserEmail(Integer otp) {
        OtpConfirmationToken otpToken = otpConfirmationRepo.findByOtp(otp);
        if (otpToken!= null) {
            return otpToken.getUser().getEmail();
        }
        return null;
    }
}
