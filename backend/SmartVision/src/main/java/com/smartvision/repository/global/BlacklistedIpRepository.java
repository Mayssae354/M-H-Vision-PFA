package com.smartvision.repository.global;

import com.smartvision.entity.global.BlacklistedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistedIpRepository extends JpaRepository<BlacklistedIp, Long> {
    Optional<BlacklistedIp> findByIpAddress(String ipAddress);
}
