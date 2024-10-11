package com.example.recruiting_application.controller;


import com.example.recruiting_application.service.EmailService;
import com.example.recruiting_application.service.ForgetPassService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmailController {
    private EmailService service;
    private ForgetPassService forgetPassService;
    public EmailController(EmailService service, ForgetPassService forgetPassService){
        this.service = service;
        this.forgetPassService = forgetPassService;
    }

    // Endpoint to request an OTP

    @PostMapping("/password-recovery")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Simulate a situation where the user is not found
        boolean otpSent = forgetPassService.generateOtp(email);

        if (otpSent) {
            return ResponseEntity.ok(Collections.singletonMap("message", "OTP sent to your email."));
        } else {
            // Return JSON response for error, not plain text
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "User not found"));
        }
    }



    // Endpoint to verify OTP and reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam int otp, @RequestParam String newPassword) {
        boolean passwordReset = forgetPassService.verifyOtpAndResetPassword(email, otp, newPassword);
        if (passwordReset) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Password reset successful"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Invalid OTP or OTP expired"));
        }
    }


}
