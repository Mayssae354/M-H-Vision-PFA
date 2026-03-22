package com.smartvision.smartvision_backend.service.session;

import com.smartvision.smartvision_backend.dto.response.SessionStatsResponse;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Session;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import com.smartvision.smartvision_backend.repository.tenant.AttendanceRepository;
import com.smartvision.smartvision_backend.repository.tenant.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j //Ajoute un logger pour tracer les opérations
@Service //Indique que c'est un bean de service Spring
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;

    // ── Création ──────────────────────────────────────────────────────────────

    @Transactional
    public Session createSession(Session session) {
        log.info("Creating session for subject {} group {}",
                session.getSubject().getId(), session.getGroup().getId());
        session.setStatus(SessionStatus.PLANNED);
        return sessionRepository.save(session);
    }

    // ── Cycle de vie ──────────────────────────────────────────────────────────

    @Transactional
    public Session openSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        if (session.getStatus() != SessionStatus.PLANNED) {
            throw new IllegalStateException(
                    "Session " + sessionId + " ne peut pas être ouverte (statut: " + session.getStatus() + ")");
        }
        session.setStatus(SessionStatus.ACTIVE);
        log.info("Session {} opened", sessionId);
        return sessionRepository.save(session);
    }

    @Transactional
    public Session closeSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Session " + sessionId + " n'est pas active (statut: " + session.getStatus() + ")");
        }
        session.setStatus(SessionStatus.CLOSED);
        session.setEndTime(ZonedDateTime.now());
        log.info("Session {} closed", sessionId);
        return sessionRepository.save(session);
    }

    @Transactional
    public Session cancelSession(Long sessionId) {
        Session session = getOrThrow(sessionId);
        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new IllegalStateException("Impossible d'annuler une session déjà fermée.");
        }
        session.setStatus(SessionStatus.CANCELLED);
        log.info("Session {} cancelled", sessionId);
        return sessionRepository.save(session);
    }

    // ── Lecture ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Session getById(Long sessionId) {
        return getOrThrow(sessionId);
    }

    @Transactional(readOnly = true)
    public List<Session> getByGroup(Long groupId) {
        return sessionRepository.findByGroupId(groupId);
    }

    @Transactional(readOnly = true)
    public List<Session> getByProfessor(String professorCin) {
        return sessionRepository.findByProfessorCin(professorCin);
    }

    @Transactional(readOnly = true)
    public List<Session> getByProfessorAndDateRange(String cin,
                                                    ZonedDateTime from,
                                                    ZonedDateTime to) {
        return sessionRepository.findByProfessorCinAndDateRange(cin, from, to);
    }

    /**
     * Retourne la session ACTIVE du professeur (pour le live overlay).
     */
    @Transactional(readOnly = true)
    public Session getActiveSessionForProfessor(String professorCin) {
        return sessionRepository
                .findByProfessorCinAndStatus(professorCin, SessionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune session active pour le professeur " + professorCin));
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public SessionStatsResponse getStats(Long sessionId) {
        Session session = getOrThrow(sessionId);

        List<Object[]> rawStats = attendanceRepository.countByStatusForSession(sessionId); //La requête retourne une liste de tableaux d'objets où chaque tableau contient 2 éléments :
                                                                                            //row[0] : Le statut de présence (PRESENT, ABSENT, LATE, JUSTIFIED) - type String ou Enum
                                                                                            //row[1] : Le nombre d'étudiants pour ce statut - type Long

        Map<String, Integer> statusCounts = new java.util.HashMap<>(); //Le code convertit ces données brutes en une Map plus facile à manipuler
        for (Object[] row : rawStats) {
            statusCounts.put(row[0].toString(), ((Long) row[1]).intValue());
        }

        int present   = statusCounts.getOrDefault(AttendanceStatus.PRESENT.name(),   0);
        int absent    = statusCounts.getOrDefault(AttendanceStatus.ABSENT.name(),    0);
        int late      = statusCounts.getOrDefault(AttendanceStatus.LATE.name(),      0);
        int justified = statusCounts.getOrDefault(AttendanceStatus.JUSTIFIED.name(), 0);
        int total     = present + absent + late + justified;
        double rate   = total > 0 ? (present + late) * 100.0 / total : 0.0;

        return SessionStatsResponse.builder()
                .sessionId(sessionId)
                .status(session.getStatus())
                .room(session.getRoom())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .createdAt(session.getCreatedAt())
                .subjectId(session.getSubject().getId())
                .subjectName(session.getSubject().getName())
                .subjectCode(session.getSubject().getCode())
                .semester(session.getSubject().getSemester())
                .professorCin(session.getProfessorCin())
                .groupId(session.getGroup().getId())
                .groupName(session.getGroup().getName())
                .groupCode(session.getGroup().getCode())
                .academicYear(session.getGroup().getAcademicYear())
                .totalStudents(total)
                .totalMembers(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .justifiedCount(justified)
                .attendanceRate(rate)
                .statusCounts(statusCounts)
                .build();
    }

    // ── Utilitaire interne ────────────────────────────────────────────────────

    private Session getOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session introuvable : " + id));
    }
}