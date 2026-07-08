package com.food.recipeflow.service;

import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, OtpInfo> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Value("${otp.expiration.minutes:5}")
    private int otpExpirationMinutes;

    public OtpService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    private static class OtpInfo {
        String otp;
        long expirationTime;

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

        sendEmail(email, otp);
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("RecipeFlow - Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp + "\nThis OTP will expire in " + otpExpirationMinutes + " minutes.");
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
    }
}
