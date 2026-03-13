package com.smartvision.repository.global;

import com.smartvision.entity.global.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {
    Optional<UserOrganization> findByUserIdAndOrganizationId(Long userId, Long organizationId);
    List<UserOrganization> findByUserId(Long userId);
}
