package com.smartvision.smartvision_backend.entity.global;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_ips", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false, unique = true, length = 45)
    private String ipAddress;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_by")
    private User blockedBy;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        blockedAt = LocalDateTime.now();
    }

    // ── Méthodes utilitaires ───────────────────────────
    public boolean isActive() {
        if (expiresAt == null) {
            return true; // bloquée définitivement
        }
        return expiresAt.isAfter(LocalDateTime.now());
    }

    public boolean isExpired() {
        return expiresAt != null
                && expiresAt.isBefore(LocalDateTime.now());
    }
}
