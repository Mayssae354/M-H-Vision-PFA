package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "absence_contests")
@Data //getters + setters + toString + equals
@NoArgsConstructor //constructeur vide
@AllArgsConstructor //constructeur avec tous les champs
@Builder //permet de créer un objet avec un builder pattern
public class AbsenceContest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY → auto-incrément dans la base
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //LAZY : charge l’objet seulement si nécessaire
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    // String car users est dans le schema global
    @Column(name = "member_cin", nullable = false, length = 20)
    private String memberCin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "proof_url", length = 500)
    private String proofUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ContestStatus status = ContestStatus.PENDING;

    // CIN du prof/admin qui a reviewé — String car schema global
    @Column(name = "reviewed_by", length = 20)
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private ZonedDateTime reviewedAt;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }

    public enum ContestStatus {
        PENDING, APPROVED, REJECTED
    }
}