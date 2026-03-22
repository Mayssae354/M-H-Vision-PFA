package com.smartvision.smartvision_backend.service.attendance;

import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest;
import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest.ContestStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance;
import com.smartvision.smartvision_backend.repository.tenant.AbsenceContestRepository;
import com.smartvision.smartvision_backend.repository.tenant.AttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceContestRepository contestRepository;
    private final AttendanceRepository     attendanceRepository;
    private final AttendanceService        attendanceService;

    // ── Dépôt de contestation ─────────────────────────────────────────────────

    /**
     * Un étudiant conteste une absence. Une seule contestation par absence.
     */
    @Transactional
    public AbsenceContest submitContest(Long attendanceId,
                                        String memberCin,
                                        String reason,
                                        String proofUrl)
    {
        if (contestRepository.existsByAttendanceId(attendanceId)) {
            throw new IllegalStateException(
                    "Une contestation existe déjà pour cette absence.");
        }

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attendance introuvable : " + attendanceId));

        // Vérifier que l'absence appartient bien au membre
        if (!attendance.getMemberCin().equals(memberCin)) {
            throw new SecurityException("Cette absence n'appartient pas au membre " + memberCin);
        }

        AbsenceContest contest = AbsenceContest.builder()
                .attendance(attendance)
                .memberCin(memberCin)
                .reason(reason)
                .proofUrl(proofUrl)
                .status(ContestStatus.PENDING)
                .build();

        log.info("Contest submitted by {} for attendance {}", memberCin, attendanceId);
        return contestRepository.save(contest);
    }

    // ── Traitement par admin/prof ─────────────────────────────────────────────

    @Transactional
    public AbsenceContest approveContest(Long contestId, String reviewerCin) {
        AbsenceContest contest = getOrThrow(contestId);
        assertPending(contest);

        contest.setStatus(ContestStatus.APPROVED);
        contest.setReviewedBy(reviewerCin);
        contest.setReviewedAt(ZonedDateTime.now());

        // Justifie automatiquement l'absence associée
        attendanceService.justifyAbsence(contest.getAttendance().getId());

        log.info("Contest {} approved by {}", contestId, reviewerCin);
        return contestRepository.save(contest);
    }

    @Transactional
    public AbsenceContest rejectContest(Long contestId, String reviewerCin) {
        AbsenceContest contest = getOrThrow(contestId);
        assertPending(contest);

        contest.setStatus(ContestStatus.REJECTED);
        contest.setReviewedBy(reviewerCin);
        contest.setReviewedAt(ZonedDateTime.now());

        log.info("Contest {} rejected by {}", contestId, reviewerCin);
        return contestRepository.save(contest);
    }

    // ── Lecture ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AbsenceContest> getPendingContests() {
        return contestRepository.findByStatus(ContestStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<AbsenceContest> getContestsByMember(String memberCin) {
        return contestRepository.findByMemberCin(memberCin);
    }

    @Transactional(readOnly = true)
    public AbsenceContest getContestForAttendance(Long attendanceId) {
        return contestRepository.findByAttendanceId(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune contestation pour l'absence : " + attendanceId));
    }

    // ── Utilitaires internes ──────────────────────────────────────────────────

    private AbsenceContest getOrThrow(Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Contestation introuvable : " + id));
    }

    private void assertPending(AbsenceContest contest) {
        if (contest.getStatus() != ContestStatus.PENDING) {
            throw new IllegalStateException(
                    "La contestation a déjà été traitée : " + contest.getStatus());
        }
    }
}