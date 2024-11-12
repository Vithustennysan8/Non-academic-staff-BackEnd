package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Entity.OtpConfirmationToken;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Mail.MailService;
import com.Non_academicWebsite.Repository.OtpConfirmationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OtpConfirmationService {
    @Autowired
    private OtpConfirmationRepo otpConfirmationRepo;
    @Autowired
    private MailService mailService;

    public void sendOtp(User user, String email) {
        Integer otp = generateOtp();
        OtpConfirmationToken otpToken = OtpConfirmationToken.builder()
                .user(user)
                .createdAt(new Date())
                .otp(otp)
                .updatedAt(new Date())
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
        if (otpToken == null || otpToken.getUpdatedAt().getTime() < new Date().getTime() - (60 * 1000 * 5)) {
            return "Invalid OTP or OTP expired";
        }
        otpToken.setConfirmedAt(new Date());
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
