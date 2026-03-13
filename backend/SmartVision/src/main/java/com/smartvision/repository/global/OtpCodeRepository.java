package com.smartvision.repository.global;

import com.smartvision.entity.global.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    List<OtpCode> findByEmailAndIsUsedFalse(String email);
}
