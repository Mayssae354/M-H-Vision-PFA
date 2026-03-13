package com.smartvision.service.attendance;

import com.smartvision.repository.tenant.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public void markAttendance() {
        // Logic to validate session and mark attendance for user
    }
}
