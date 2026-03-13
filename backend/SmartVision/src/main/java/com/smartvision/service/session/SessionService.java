package com.smartvision.service.session;

import com.smartvision.repository.tenant.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public void startSession(Long sessionId) {
        // Logic to update session status to ACTIVE
    }

    public void endSession(Long sessionId) {
        // Logic to close session and summarize attendance
    }
}
