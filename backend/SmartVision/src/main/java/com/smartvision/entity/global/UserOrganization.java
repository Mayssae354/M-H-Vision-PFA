package com.smartvision.entity.global;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_organizations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "org_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cin", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @Column(name = "joined_at", updatable = false)
    private ZonedDateTime joinedAt;

    @Column(name = "suspended_at")
    private ZonedDateTime suspendedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    public UserOrganization() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ZonedDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(ZonedDateTime joinedAt) { this.joinedAt = joinedAt; }

    public ZonedDateTime getSuspendedAt() { return suspendedAt; }
    public void setSuspendedAt(ZonedDateTime suspendedAt) { this.suspendedAt = suspendedAt; }

    public User getInvitedBy() { return invitedBy; }
    public void setInvitedBy(User invitedBy) { this.invitedBy = invitedBy; }

    @PrePersist
    protected void onCreate() {
        joinedAt = ZonedDateTime.now();
    }

    public enum Role {
        SUPER_ADMIN, ADMIN, PROFESSOR, STUDENT, EMPLOYEE, RESEARCHER, MANAGER, HR
    }

    public enum Status {
        ACTIVE, SUSPENDED, PENDING
    }
}