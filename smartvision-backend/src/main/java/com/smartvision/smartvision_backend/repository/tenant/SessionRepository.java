package com.smartvision.smartvision_backend.repository.tenant;

import com.smartvision.smartvision_backend.entity.tenant.Session;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    // Session active d'un prof (pour le live overlay)
    Optional<Session> findByProfessorCinAndStatus(String professorCin, SessionStatus status);

    // Sessions d'un groupe
    List<Session> findByGroupId(Long groupId);

    // Sessions d'un prof
    List<Session> findByProfessorCin(String professorCin);

    // Sessions par statut
    List<Session> findByStatus(SessionStatus status);

    // Sessions dans un intervalle de temps
    List<Session> findByStartTimeBetween(ZonedDateTime from, ZonedDateTime to);

    // Sessions d'un prof dans un intervalle
    @Query("SELECT s FROM Session s WHERE s.professorCin = :cin " +
            "AND s.startTime BETWEEN :from AND :to")
    List<Session> findByProfessorCinAndDateRange(
            @Param("cin") String professorCin,
            @Param("from") ZonedDateTime from,
            @Param("to") ZonedDateTime to
    );

    // Compter les sessions d'un sujet
    long countBySubjectId(Long subjectId);
}