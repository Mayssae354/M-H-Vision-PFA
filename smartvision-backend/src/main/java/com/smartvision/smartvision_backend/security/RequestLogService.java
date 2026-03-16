package com.smartvision.smartvision_backend.security;


import com.smartvision.smartvision_backend.entity.global.SecurityLog;
import com.smartvision.smartvision_backend.repository.global.SecurityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLogService {

    private final SecurityLogRepository securityLogRepository;

    @Async
    public void logRequest(HttpServletRequest  request,
                           HttpServletResponse response,
                           long startTime) {
        try {
            int responseMs = (int)(
                    System.currentTimeMillis() - startTime);

            SecurityLog secLog = SecurityLog.createLog(
                    getClientIp(request),
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus(),
                    responseMs,
                    request.getHeader("User-Agent"));

// Si 401 ou 403 → marquer comme suspect
            if (response.getStatus() == 401
                    || response.getStatus() == 403) {
                secLog.setSuspicious(true);
            }

        } catch (Exception e) {
            log.error("Erreur logging requête : {}",
                    e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}