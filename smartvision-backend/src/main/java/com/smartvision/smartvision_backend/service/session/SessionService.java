package com.smartvision.smartvision_backend.service.session;

import com.smartvision.smartvision_backend.dto.response.SessionStatsResponse;
import com.smartvision.smartvision_backend.entity.tenant.Session;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.repository.tenant.AttendanceRepository;
import com.smartvision.smartvision_backend.repository.tenant.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;

    // Démarrer une session (PLANNED → ACTIVE)
    @Transactional
    public Session startSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        if (session.getStatus() != SessionStatus.PLANNED) {
            throw new IllegalStateException("La session n'est pas en état PLANNED");
        }
        session.setStatus(SessionStatus.ACTIVE);
        return sessionRepository.save(session);
    }

    // Clôturer une session (ACTIVE → CLOSED)
    @Transactional
    public Session closeSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalStateException("La session n'est pas ACTIVE");
        }
        session.setStatus(SessionStatus.CLOSED);
        return sessionRepository.save(session);
    }

    // Annuler une session
    @Transactional
    public Session cancelSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        session.setStatus(SessionStatus.CANCELLED);
        return sessionRepository.save(session);
    }

    // Récupérer la session active d'un prof
    public Session getActiveSessionForProfessor(String professorCin) {
        return sessionRepository
                .findByProfessorCinAndStatus(professorCin, SessionStatus.ACTIVE)
                .orElseThrow(() -> new NoSuchElementException(
                        "Aucune session active pour ce professeur"
                ));
    }

    // Stats d'une session
    public SessionStatsResponse getSessionStats(Long sessionId) {
        Session session = getOrThrow(sessionId);
        List<Object[]> rawStats = attendanceRepository.countByStatusForSession(sessionId);

        // Construire map statut → count
        Map<String, Long> statsMap = rawStats.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        int present   = statsMap.getOrDefault("PRESENT",   0L).intValue();
        int absent    = statsMap.getOrDefault("ABSENT",    0L).intValue();
        int late      = statsMap.getOrDefault("LATE",      0L).intValue();
        int justified = statsMap.getOrDefault("JUSTIFIED", 0L).intValue();
        int total     = present + absent + late + justified;
        double rate   = total > 0 ? (double)(present + late) / total * 100 : 0;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return SessionStatsResponse.builder()
                .sessionId(session.getId())
                .subjectName(session.getSubject().getName())
                .groupName(session.getGroup().getName())
                .room(session.getRoom())
                .totalMembers(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .justifiedCount(justified)
                .attendanceRate(Math.round(rate * 10.0) / 10.0)
                .sessionStatus(session.getStatus().name())
                .startTime(session.getStartTime().format(fmt))
                .endTime(session.getEndTime().format(fmt))
                .build();
    }

    public List<Session> getSessionsByProfessor(String professorCin) {
        return sessionRepository.findByProfessorCin(professorCin);
    }

    public List<Session> getSessionsByGroup(Long groupId) {
        return sessionRepository.findByGroupId(groupId);
    }

    public Session getOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Session introuvable : " + id));
    }
}