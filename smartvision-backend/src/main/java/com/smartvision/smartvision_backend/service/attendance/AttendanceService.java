package com.smartvision.smartvision_backend.service.attendance;

import com.smartvision.smartvision_backend.dto.request.AttendanceRequest;
import com.smartvision.smartvision_backend.entity.tenant.Attendance;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Session;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import com.smartvision.smartvision_backend.repository.tenant.AttendanceRepository;
import com.smartvision.smartvision_backend.repository.tenant.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {


    private final AttendanceRepository attendanceRepository;
    private final SessionRepository    sessionRepository;

    // ── Enregistrement ────────────────────────────────────────────────────────

    /**
     * Enregistre ou met à jour la présence d'un membre dans une session.
     * Idempotent : si l'entrée existe déjà, elle est mise à jour.
     */

    @Transactional
    public Attendance recordAttendance(AttendanceRequest req) {
        Session session = sessionRepository.findById(req.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Session introuvable : " + req.getSessionId()));

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalStateException(
                    "La session n'est pas active. Statut actuel : " + session.getStatus());
        }

        // Idempotence : update si déjà enregistré
        Attendance attendance = attendanceRepository
                .findBySessionIdAndMemberCin(req.getSessionId(), req.getMemberCin())
                .orElse(Attendance.builder()
                        .session(session)
                        .memberCin(req.getMemberCin())
                        .build());

        attendance.setStatus(req.getStatus());
        attendance.setDetectedBy(req.getDetectedBy());
        attendance.setConfidence(req.getConfidence());

        log.info("Attendance recorded: member={} session={} status={}",
                req.getMemberCin(), req.getSessionId(), req.getStatus());

        return attendanceRepository.save(attendance);
    }

    /**
     * Enregistrement en lot — utilisé par la reconnaissance faciale pour
     * une liste entière d'étudiants détectés.
     */
    @Transactional
    public List<Attendance> recordBatch(List<AttendanceRequest> requests) {
        return requests.stream()
                .map(this::recordAttendance)
                .toList();
    }

    // ── Lecture ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Attendance> getBySession(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getByMember(String memberCin) {
        return attendanceRepository.findByMemberCin(memberCin);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAbsencesByMemberAndSubject(String memberCin, Long subjectId) {
        return attendanceRepository.findAbsencesByMemberAndSubject(memberCin, subjectId);
    }

    @Transactional(readOnly = true)
    public long countAbsences(String memberCin) {
        return attendanceRepository.countByMemberCinAndStatus(memberCin, AttendanceStatus.ABSENT);
    }

    // ── Modification manuelle ─────────────────────────────────────────────────

    @Transactional
    public Attendance updateStatus(Long attendanceId, AttendanceStatus newStatus) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attendance introuvable : " + attendanceId));
        attendance.setStatus(newStatus);
        log.info("Attendance {} updated to {}", attendanceId, newStatus);
        return attendanceRepository.save(attendance);
    }

    /**
     * Marque un étudiant comme JUSTIFIED suite à l'approbation
     * d'une contestation d'absence.
     */
    @Transactional
    public Attendance justifyAbsence(Long attendanceId) {
        return updateStatus(attendanceId, AttendanceStatus.JUSTIFIED);
    }
}