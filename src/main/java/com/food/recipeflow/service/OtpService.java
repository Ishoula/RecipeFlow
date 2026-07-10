package com.food.recipeflow.service;

import java.io.IOException;
import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
    private final UserRepository userRepository;
    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;
    private final ConcurrentHashMap<String, OtpInfo> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Value("${otp.expiration.minutes:15}")
    private int otpExpirationMinutes;

    @Value("${otp.from.email:}")
    private String otpFromEmail;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public OtpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        logger.info("OtpService initialized with:");
        logger.info("  OTP Expiration: {} minutes", otpExpirationMinutes);
        logger.info("  OTP From Email: {}",
                otpFromEmail != null && !otpFromEmail.isEmpty() ? otpFromEmail : "Using default (mail username)");
        logger.info("  Mail Username configured: {}", mailUsername != null && !mailUsername.isEmpty() ? "Yes" : "No");
    }

    private static class OtpInfo {
        final String otp;
        final long expirationTime;

        OtpInfo(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }
    }

    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + (otpExpirationMinutes * 60 * 1000L);
        otpStorage.put(email, new OtpInfo(otp, expirationTime));

        try {
            sendEmail(email, otp);
            logger.info("OTP sent successfully to {}", email);
        } catch (Exception e) {
            logger.error("Failed to send OTP to {}, falling back to logging OTP", email, e);
            logger.warn("========================================");
            logger.warn("OTP for {} is: {}", email, otp);
            logger.warn("========================================");
        }
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendEmail(String to, String otp) throws IOException {

        Email from = new Email(otpFromEmail);
        Email recipient = new Email(to);

        String subject = "RecipeFlow - Email Verification";

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="margin:0;padding:0;background:#f4f4f4;font-family:Arial,Helvetica,sans-serif;">

                    <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
                        <tr>
                            <td align="center">

                                <table width="600" cellpadding="0" cellspacing="0"
                                       style="background:#ffffff;border-radius:12px;overflow:hidden;
                                              box-shadow:0 4px 12px rgba(0,0,0,.08);">

                                    <!-- Header -->
                                    <tr>
                                        <td style="background:#4CAF50;padding:30px;text-align:center;color:white;">
                                            <h1 style="margin:0;">🍽️ RecipeFlow</h1>
                                            <p style="margin:8px 0 0 0;font-size:16px;">
                                                Verify Your Email
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Body -->
                                    <tr>
                                        <td style="padding:40px;color:#333333;">

                                            <h2 style="margin-top:0;">
                                                Hello!
                                            </h2>

                                            <p style="font-size:16px;line-height:1.6;">
                                                Thank you for creating a RecipeFlow account.
                                                Please use the verification code below to complete
                                                your registration.
                                            </p>

                                            <div style="
                                                margin:35px auto;
                                                background:#f5f5f5;
                                                border:2px dashed #4CAF50;
                                                border-radius:10px;
                                                padding:20px;
                                                width:220px;
                                                text-align:center;">

                                                <span style="
                                                    font-size:34px;
                                                    font-weight:bold;
                                                    letter-spacing:8px;
                                                    color:#4CAF50;">
                                                    %s
                                                </span>

                                            </div>

                                            <p style="font-size:15px;color:#555;">
                                                This verification code expires in
                                                <strong>%d minutes</strong>.
                                            </p>

                                            <p style="font-size:15px;color:#555;">
                                                If you didn't request this email,
                                                you can safely ignore it.
                                            </p>

                                            <hr style="margin:35px 0;border:none;border-top:1px solid #e5e5e5;">

                                            <p style="font-size:13px;color:#888;">
                                                For your security, never share this code with anyone.
                                            </p>

                                        </td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td style="
                                            background:#fafafa;
                                            text-align:center;
                                            padding:20px;
                                            color:#888;
                                            font-size:13px;">

                                            © 2026 RecipeFlow<br>
                                            Discover • Cook • Share

                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>
                    </table>

                </body>
                </html>
                """.formatted(otp, otpExpirationMinutes);

        Content content = new Content("text/html", html);

        Mail mail = new Mail(from, subject, recipient, content);

        SendGrid sg = new SendGrid(sendGridApiKey);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        if (response.getStatusCode() >= 400) {
            throw new RuntimeException(
                    "SendGrid Error: " +
                            response.getStatusCode() +
                            " " +
                            response.getBody());
        }
    }

    public void verifyOtp(String email, String otp) {
        OtpInfo otpInfo = otpStorage.get(email);
        if (otpInfo == null) {
            throw new RuntimeException("OTP not found for email: " + email);
        }

        if (System.currentTimeMillis() > otpInfo.expirationTime) {
            otpStorage.remove(email);
            throw new RuntimeException("OTP has expired");
        }

        if (!otpInfo.otp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        userRepository.save(user);

        otpStorage.remove(email);
        logger.info("Email verified successfully for: {}", email);
    }
}
