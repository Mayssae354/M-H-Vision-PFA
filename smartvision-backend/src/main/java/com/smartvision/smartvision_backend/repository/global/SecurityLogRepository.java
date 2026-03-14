package com.smartvision.smartvision_backend.repository.global;

import com.smartvision.smartvision_backend.entity.global.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SecurityLogRepository
        extends JpaRepository<SecurityLog, Long> {

    List<SecurityLog> findByIpAddress(String ipAddress);

    @Query("SELECT s FROM SecurityLog s " +
            "WHERE s.ipAddress = :ip " +
            "AND s.statusCode >= 400 " +
            "AND s.timestamp >= :since")
    List<SecurityLog> findFailedAttemptsByIp(
            String ip,
            LocalDateTime since);

    @Query("SELECT s FROM SecurityLog s " +
            "WHERE s.isSuspicious = true " +
            "AND s.timestamp >= :since " +
            "ORDER BY s.timestamp DESC")
    List<SecurityLog> findSuspiciousLogs(LocalDateTime since);

    @Query("SELECT s FROM SecurityLog s " +
            "WHERE s.organization.id = :orgId " +
            "AND s.timestamp >= :since")
    List<SecurityLog> findByOrgIdSince(
            Long orgId,
            LocalDateTime since);
}