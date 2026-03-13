package com.smartvision.service.attendance;

import com.smartvision.repository.tenant.AbsenceContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceContestRepository absenceContestRepository;

    public void processAbsenceContest() {
        // Logic to review absence contest
    }
}
