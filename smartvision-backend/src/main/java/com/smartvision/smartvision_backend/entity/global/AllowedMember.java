package com.smartvision.smartvision_backend.entity.global;

import com.smartvision.smartvision_backend.entity.global.Organization;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "allowed_members", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowedMember {

    @Id
    @Column(name = "cin", length = 20)
    private String cin;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserOrganization.Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NOT_REGISTERED;

    @Column(name = "invite_token", unique = true)
    private UUID inviteToken;

    @Column(name = "token_expires")
    private LocalDateTime tokenExpires;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    // ── Méthodes utilitaires ───────────────────────────
    public boolean isTokenValid() {
        return inviteToken != null
                && tokenExpires != null
                && tokenExpires.isAfter(LocalDateTime.now())
                && status == Status.INVITED;
    }

    public boolean isAlreadyRegistered() {
        return status == Status.REGISTERED;
    }

    public enum Status {
        NOT_REGISTERED, INVITED, REGISTERED
    }
}