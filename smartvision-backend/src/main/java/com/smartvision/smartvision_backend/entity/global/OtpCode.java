package com.smartvision.smartvision_backend.entity.global;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpType type;

    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Méthodes utilitaires ───────────────────────────
    public boolean isValid() {
        return !isUsed
                && expiresAt.isAfter(LocalDateTime.now());
    }

    public void markAsUsed() {
        this.isUsed = true;
    }

    public enum OtpType {
        REGISTRATION, LOGIN, RESET_PASSWORD
    }
}