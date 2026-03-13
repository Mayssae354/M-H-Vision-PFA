package com.smartvision.repository.global;

import com.smartvision.entity.global.AllowedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllowedMemberRepository extends JpaRepository<AllowedMember, Long> {
    Optional<AllowedMember> findByEmailAndOrganizationId(String email, Long organizationId);
}
