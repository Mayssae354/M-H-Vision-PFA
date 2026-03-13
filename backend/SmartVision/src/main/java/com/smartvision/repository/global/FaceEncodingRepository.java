package com.smartvision.repository.global;

import com.smartvision.entity.global.FaceEncoding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaceEncodingRepository extends JpaRepository<FaceEncoding, Long> {
    Optional<FaceEncoding> findByUserId(Long userId);
}
