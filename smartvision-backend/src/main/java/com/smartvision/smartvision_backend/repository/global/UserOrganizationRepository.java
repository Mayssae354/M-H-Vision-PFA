package com.smartvision.smartvision_backend.repository.global;

import com.smartvision.smartvision_backend.entity.global.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrganizationRepository
        extends JpaRepository<UserOrganization, Long> {

    Optional<UserOrganization> findByUserCinAndOrganizationId(
            String cin, Long orgId);

    List<UserOrganization> findByUserCin(String cin);

    List<UserOrganization> findByOrganizationId(Long orgId);

    boolean existsByUserCinAndOrganizationId(String cin, Long orgId);

    @Query("SELECT uo FROM UserOrganization uo " +
            "WHERE uo.user.cin = :cin " +
            "AND uo.status = 'ACTIVE'")
    List<UserOrganization> findActiveOrgsByUserCin(String cin);
}