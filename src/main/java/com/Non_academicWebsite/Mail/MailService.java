package com.Non_academicWebsite.Mail;

import com.Non_academicWebsite.Entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String hostMail;

    @Value("${spring.mail.password}")
    private String mailAppPassword;

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Async
    public void sendMail( String to, String url, String employeeName, String formType,
                         String status , String approverName) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String emailContent = formStatusMailContent(employeeName, formType, url, approverName, status);
            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject("Verify the register");
            helper.setFrom(hostMail);


            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send mail", e);
            throw new IllegalStateException("Failed to send mail");
        }
    }

    @Async
    public void sendMailForRegister(String to, String url, User user, String approver) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String emailContent = emailVerifiedHtmlContent(user.getFirst_name(), user.getFaculty(), user.getDepartment(), approver, url);
            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject("Verify the register");
            helper.setFrom(hostMail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send mail", e);
            throw new IllegalStateException("Failed to send mail");
        }
    }

    @Async
    public void sendMailForOTP(Integer OTP, String Name, String to){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String emailContent = otpHtmlContent(Name, OTP);
            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject("OTP Verification");
            helper.setFrom(hostMail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send mail", e);
            throw new IllegalStateException("Failed to send mail");
        }
    }

    private String otpHtmlContent(String name, Integer otp) {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" " +
                "content=\"width=device-width, initial-scale=1.0\"><title>OTP Verification</title>" +
                "<style>body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333333; }" +
                ".container { max-width: 600px; margin: 50px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; " +
                "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }.header { text-align: center; padding-bottom: 20px; border-bottom: 1px " +
                "solid #dddddd; }.header h1 { margin: 0; font-size: 24px; color: #28a745; }.content { padding: 20px; line-height: 1.6; " +
                ".content p { margin: 0 0 20px; }.btn { display: inline-block; background-color: #28a745; color: #ffffff; padding: 10px " +
                "20px; text-decoration: none; border-radius: 5px; font-weight: bold; }.footer { text-align: center; padding-top: 20px;" +
                " border-top: 1px solid #dddddd; font-size: 12px; color: #777777; }</style></head><body><div class=\"container\">" +
                "<div class=\"header\"><h1>OTP Verification</h1></div>" +
                "<div class=\"content\"><p>Dear " + name + ",</p>" +
                "<p>Please enter the OTP (" + otp + ") on your registered email to verify your account.</p>" +
                "<p>If you have not received the OTP, please check your spam folder or contact us at <a href=\"mailto:admin@university.edu\">admin@university.edu</a>.</p></div>" +
                "<div class=\"footer\"><p>&copy; 2024 University of Peradinya. All rights reserved.</p></div></div></body></html>";
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

    public String emailVerifiedHtmlContent(String name, String department, String faculty,String approver, String url) {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" " +
                "content=\"width=device-width, initial-scale=1.0\"><title>Registration Success</title>" +
                "<style>body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333333; }" +
                ".container { max-width: 600px; margin: 50px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; " +
                "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }.header { text-align: center; padding-bottom: 20px; border-bottom: 1px " +
                "solid #dddddd; }.header h1 { margin: 0; font-size: 24px; color: #28a745; }.content { padding: 20px; line-height: 1.6; " +
                "}.content p { margin: 0 0 20px; }.btn { display: inline-block; background-color: #28a745; color: #ffffff; padding: 10px " +
                "20px; text-decoration: none; border-radius: 5px; font-weight: bold; }.footer { text-align: center; padding-top: 20px;" +
                " border-top: 1px solid #dddddd; font-size: 12px; color: #777777; }</style></head><body><div class=\"container\">" +
                "<div class=\"header\"><h1>Registration Successful</h1></div>" +
                "<div class=\"content\"><p>Dear " + name + ",</p>" +
                "<p>Your registration has been successfully completed. You are now registered in the " + department + " department of the " + faculty + " faculty.</p>" +
                "<p>Your approver for future requests will be " + approver + ".</p>" +
                "<p>Please click the link below to visit your dashboard and manage your account:</p>" +
                "<p style=\"text-align: center;\"><a href=\"" + url + "\" class=\"btn\">Go to Dashboard</a></p>" +
                "<p>If the button above does not work, please copy and paste the following link into your browser:</p>" +
                "<p><a href=\"" + url + "\">" + url + "</a></p>" +
                "<p>If you have any further questions, feel free to reach out to us.</p></div>" +
                "<div class=\"footer\"><p>&copy; 2024 University of Peradinya. All rights reserved.</p></div></div></body></html>";

    }

    public String formStatusMailContent(String employeeName, String leaveType,
                                        String applicationLink, String approverName, String approvalStatus) {
        return  "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "    <meta charset='UTF-8'>"
                + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "    <title>Leave Application Status</title>"
                + "    <style>"
                + "        body {"
                + "            font-family: Arial, sans-serif;"
                + "            background-color: #1e1e2f;" /* Dark background */
                + "            color: #f0f0f0;" /* Light text color for contrast */
                + "            line-height: 1.6;"
                + "        }"
                + "        .container {"
                + "            max-width: 600px;"
                + "            margin: 0 auto;"
                + "            background-color: #2c2c3a;" /* Dark container background */
                + "            padding: 20px;"
                + "            border-radius: 8px;"
                + "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);"
                + "        }"
                + "        h1 {"
                + "            color: #5db3ff;" /* Bright blue for headings */
                + "        }"
                + "        p {"
                + "            font-size: 16px;"
                + "            color: #d0d0d0;" /* Slightly muted text color */
                + "        }"
                + "        .button {"
                + "            background-color: #ff9f43;" /* Bright orange button */
                + "            color: #1e1e2f;" /* Dark text for contrast */
                + "            padding: 10px 20px;"
                + "            text-decoration: none;"
                + "            display: inline-block;"
                + "            margin-top: 20px;"
                + "            border-radius: 5px;"
                + "            font-weight: bold;"
                + "            box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);"
                + "        }"
                + "        .button:hover {"
                + "            background-color: #ffae57;" /* Lighter orange on hover */
                + "        }"
                + "        .footer {"
                + "            margin-top: 30px;"
                + "            font-size: 12px;"
                + "            color: #888;" /* Muted footer text */
                + "        }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <h1>"+ leaveType +" Application Approved</h1>"
                + "        <p>Dear " + employeeName + ",</p>"
                + "        <p>Your leave application has been <strong>" + approvalStatus + "</strong> by the approver.</p>"
                + "        <p><strong>Leave Type:</strong> " + leaveType + "</p>"
                + "        <a href='" + applicationLink + "' class='button'>View Details</a>"
                + "        <div class='footer'>"
                + "            <p>Best regards,</p>"
                + "            <p>" + approverName + "</p>"
                + "            <p>Non-Academic Staff Leave Management System</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";


    }

}
