package com.smartvision.service.security;

import com.smartvision.repository.global.SecurityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityMonitorService {

    private final SecurityLogRepository securityLogRepository;

    public void logSuspiciousActivity() {
        // Logic to save suspicious access to DB (like bad IPs or brute force)
    }
}
