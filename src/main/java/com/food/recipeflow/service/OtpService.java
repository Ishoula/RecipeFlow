package com.food.recipeflow.service;

import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, OtpInfo> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Value("${otp.expiration.minutes:15}")
    private int otpExpirationMinutes;

    @Value("${otp.from.email:}")
    private String otpFromEmail;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public OtpService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
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

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        String fromAddress = (otpFromEmail != null && !otpFromEmail.isEmpty()) ? otpFromEmail : mailUsername;
        if (fromAddress != null && !fromAddress.isEmpty()) {
            message.setFrom(fromAddress);
        }
        message.setTo(to);
        message.setSubject("RecipeFlow - Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp + "\nThis OTP will expire in "
                + otpExpirationMinutes + " minutes.");
        mailSender.send(message);
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
