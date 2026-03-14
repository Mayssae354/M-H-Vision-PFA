package com.smartvision.smartvision_backend.repository.tenant;

import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest;
import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest.ContestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbsenceContestRepository extends JpaRepository<AbsenceContest, Long> {

    // Toutes les contestations d'un membre
    List<AbsenceContest> findByMemberCin(String memberCin);

    // Contestation d'une absence précise
    Optional<AbsenceContest> findByAttendanceId(Long attendanceId);

    // Contestations en attente (pour admin/prof)
    List<AbsenceContest> findByStatus(ContestStatus status);

    // Contestations en attente reviewées par un prof
    List<AbsenceContest> findByReviewedBy(String reviewedByCin);

    // Vérifier si une absence a déjà une contestation
    boolean existsByAttendanceId(Long attendanceId);
}