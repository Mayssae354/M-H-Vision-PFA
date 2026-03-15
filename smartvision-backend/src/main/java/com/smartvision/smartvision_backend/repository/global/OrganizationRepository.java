package com.smartvision.smartvision_backend.repository.global;

import com.smartvision.smartvision_backend.entity.global.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartvision.smartvision_backend.entity.global.Organization.OrganizationType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findBySchemaName(String schemaName);

    Optional<Organization> findByDomainEmail(String domainEmail);

    List<Organization> findByType(OrganizationType type);

    List<Organization> findByIsActiveTrue();

    boolean existsBySchemaName(String schemaName);
}