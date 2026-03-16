package com.smartvision.smartvision_backend.entity.global;

import com.smartvision.smartvision_backend.entity.global.Organization;
import com.smartvision.smartvision_backend.entity.global.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_organizations",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_cin", "org_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Relations ──────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cin", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    // ── Champs ─────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "suspended_at")
    private LocalDateTime suspendedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    // ── Enums ──────────────────────────────────────────
    public enum Role {
        SUPER_ADMIN, ADMIN, PROFESSOR, STUDENT,
        EMPLOYEE, RESEARCHER
    }

    public enum Status {
        ACTIVE, SUSPENDED, PENDING
    }
}