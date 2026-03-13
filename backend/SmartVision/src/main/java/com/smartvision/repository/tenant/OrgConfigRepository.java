package com.smartvision.repository.tenant;

import com.smartvision.entity.tenant.OrgConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgConfigRepository extends JpaRepository<OrgConfig, Long> {
    Optional<OrgConfig> findByConfigKey(String configKey);
}
