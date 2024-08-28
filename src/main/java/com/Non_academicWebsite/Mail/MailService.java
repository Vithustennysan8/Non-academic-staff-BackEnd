package com.Non_academicWebsite.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Async
    public void sendMail(String from, String password, String to, String url, String name, String department, String faculty) {

        JavaMailSender javaMailSender = mailSender(from, password);

        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

            String emailContent = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" " +
                    "content=\"width=device-width, initial-scale=1.0\"><title>Verify Your Email</title>" +
                    "<style>body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333333; }" +
                    ".container { max-width: 600px; margin: 50px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; " +
                    "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }.header { text-align: center; padding-bottom: 20px; border-bottom: 1px " +
                    "solid #dddddd; }.header h1 { margin: 0; font-size: 24px; color: #007bff; }.content { padding: 20px; line-height: 1.6; " +
                    "}.content p { margin: 0 0 20px; }.btn { display: inline-block; background-color: #007bff; color: #ffffff; padding: 10px" +
                    " 20px; text-decoration: none; border-radius: 5px; font-weight: bold; }.footer { text-align: center; padding-top: 20px;" +
                    " border-top: 1px solid #dddddd; font-size: 12px; color: #777777; }</style></head><body><div class=\"container\">" +
                    "<div class=\"header\"><h1>Email Verification</h1></div><div class=\"content\"><p>Dear sir,</p>" +
                    "<p>I am " + name + " from " + department + " in " + faculty + ". I have registered for the online page for Non-academic staff website. Please click the button below to verify my email address and activate my account:" +
                    "</p><p style=\"text-align: center;\"><a href=\"" + url + "\" class=\"btn\">Verify Email</a></p><p>If the button above" +
                    " does not work, please copy and paste the following link into your web browser:</p><p><a href=\"" + url + "\">" + url +
                    "</a></p><p>If you have any issue with my registration, Please let me know.</p></div><div class=\"footer\"><p>&copy; " +
                    "2024 Your Company. All rights reserved.</p></div></div></body></html>";

            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject("Verify the register");
            helper.setFrom(from);

            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send mail", e);
            throw new IllegalStateException("Failed to send mail");
        }
    }

    public JavaMailSender mailSender(String email, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
