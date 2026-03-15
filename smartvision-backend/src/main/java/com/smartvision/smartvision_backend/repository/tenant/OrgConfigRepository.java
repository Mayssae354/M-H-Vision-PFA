package com.smartvision.smartvision_backend.repository.tenant;

import com.smartvision.smartvision_backend.entity.tenant.OrgConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgConfigRepository extends JpaRepository<OrgConfig, Long> {
    // méthodes custom possibles, par ex : findByConfigKey
    OrgConfig findByConfigKey(String key);
}