package com.smartvision.service.auth;

import com.smartvision.repository.global.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;
    
    public void generateAndSendOtp(String email) {
        // Logic to generate 6 digit code, save and send email
    }
}
