package com.smartvision.smartvision_backend.repository.global;

import com.smartvision.smartvision_backend.entity.global.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpCodeRepository
        extends JpaRepository<OtpCode, Long> {

    @Query("SELECT o FROM OtpCode o " +
            "WHERE o.email = :email " +
            "AND o.type = :type " +
            "AND o.isUsed = false " +
            "ORDER BY o.createdAt DESC")
    Optional<OtpCode> findLatestValidOtp(
            String email,
            OtpCode.OtpType type);

    @Modifying
    @Transactional
    @Query("DELETE FROM OtpCode o " +
            "WHERE o.email = :email " +
            "AND o.type = :type")
    void deleteAllByEmailAndType(
            String email,
            OtpCode.OtpType type);
}