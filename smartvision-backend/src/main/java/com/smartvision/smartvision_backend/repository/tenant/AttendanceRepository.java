package com.smartvision.smartvision_backend.repository.tenant;

import com.smartvision.smartvision_backend.entity.tenant.Attendance;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Présence d'un membre dans une session (unicité)
    Optional<Attendance> findBySessionIdAndMemberCin(Long sessionId, String memberCin);

    // Toutes les présences d'une session
    List<Attendance> findBySessionId(Long sessionId);

    // Historique d'un membre
    List<Attendance> findByMemberCin(String memberCin);

    // Absences d'un membre
    List<Attendance> findByMemberCinAndStatus(String memberCin, AttendanceStatus status);

    // Compter les absences d'un membre
    long countByMemberCinAndStatus(String memberCin, AttendanceStatus status);

    // Présences d'une session par statut
    List<Attendance> findBySessionIdAndStatus(Long sessionId, AttendanceStatus status);

    // Stats : nombre de présents/absents par session
    @Query("SELECT a.status, COUNT(a) FROM Attendance a " +
            "WHERE a.session.id = :sessionId GROUP BY a.status")
    List<Object[]> countByStatusForSession(@Param("sessionId") Long sessionId);

    // Absences d'un membre sur un sujet entier
    @Query("SELECT a FROM Attendance a " +
            "JOIN a.session s " +
            "WHERE s.subject.id = :subjectId " +
            "AND a.memberCin = :memberCin " +
            "AND a.status = 'ABSENT'")
    List<Attendance> findAbsencesByMemberAndSubject(
            @Param("memberCin") String memberCin,
            @Param("subjectId") Long subjectId
    );
}