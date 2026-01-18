package iuh.fit.xstore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OtpService {

    private static final int OTP_VALIDITY_MINUTES = 5;
    private static final int OTP_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 3;

    // Store: phoneNumber -> { otp, expiryTime, attempts }
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    public String generateOtp(String phoneNumber) {
        // Validate phone format: Vietnamese number starting with 0 and 10 digits
        if (!phoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits starting with 0");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", random.nextInt(999999));
        long expiryTime = System.currentTimeMillis() + (OTP_VALIDITY_MINUTES * 60 * 1000);

        OtpData otpData = new OtpData(otp, expiryTime, 0);
        otpStore.put(phoneNumber, otpData);

        // Schedule OTP cleanup after expiry
        scheduler.schedule(() -> {
            otpStore.remove(phoneNumber);
            log.info("OTP expired for phone: {}", phoneNumber);
        }, OTP_VALIDITY_MINUTES, TimeUnit.MINUTES);

        log.info("OTP generated for phone: {} (OTP: {})", phoneNumber, otp);
        
        // In production, would send via SMS provider (Twilio, AWS SNS, etc.)
        // For now, log it for demo purposes
        System.out.println("OTP for " + phoneNumber + ": " + otp);
        
        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        OtpData otpData = otpStore.get(phoneNumber);

        if (otpData == null) {
            log.warn("No OTP found for phone: {}", phoneNumber);
            return false;
        }

        // Check if expired
        if (System.currentTimeMillis() > otpData.expiryTime) {
            otpStore.remove(phoneNumber);
            log.warn("OTP expired for phone: {}", phoneNumber);
            return false;
        }

        // Check if too many attempts
        if (otpData.attempts >= MAX_ATTEMPTS) {
            otpStore.remove(phoneNumber);
            log.warn("Too many OTP verification attempts for phone: {}", phoneNumber);
            return false;
        }

        // Check OTP value
        if (!otpData.otp.equals(otp)) {
            otpData.attempts++;
            log.warn("Invalid OTP for phone: {} (Attempt: {})", phoneNumber, otpData.attempts);
            return false;
        }

        // OTP verified - remove from store
        otpStore.remove(phoneNumber);
        log.info("OTP verified successfully for phone: {}", phoneNumber);
        return true;
    }

    public void invalidateOtp(String phoneNumber) {
        otpStore.remove(phoneNumber);
        log.info("OTP invalidated for phone: {}", phoneNumber);
    }

    public boolean hasValidOtp(String phoneNumber) {
        OtpData otpData = otpStore.get(phoneNumber);
        if (otpData == null) return false;
        return System.currentTimeMillis() <= otpData.expiryTime;
    }

    // Inner class to store OTP data
    private static class OtpData {
        String otp;
        long expiryTime;
        int attempts;

        OtpData(String otp, long expiryTime, int attempts) {
            this.otp = otp;
            this.expiryTime = expiryTime;
            this.attempts = attempts;
        }
    }
}
