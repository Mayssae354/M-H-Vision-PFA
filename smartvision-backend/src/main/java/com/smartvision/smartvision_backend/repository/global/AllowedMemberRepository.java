package com.smartvision.smartvision_backend.repository.global;

import com.smartvision.smartvision_backend.entity.global.AllowedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllowedMemberRepository
        extends JpaRepository<AllowedMember, String> {

    Optional<AllowedMember> findByCin(String cin);

    Optional<AllowedMember> findByEmail(String email);

    Optional<AllowedMember> findByInviteToken(UUID token);

    boolean existsByCin(String cin);

    boolean existsByEmail(String email);
}