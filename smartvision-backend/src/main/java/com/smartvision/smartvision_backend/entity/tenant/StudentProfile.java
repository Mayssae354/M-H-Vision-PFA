package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @Column(name = "student_cin", length = 20)
    private String studentCin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(length = 100)
    private String filiere;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Niveau niveau;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Column(name = "apogee_code", unique = true, length = 20)
    private String apogeeCode;

    @Column(name = "is_repeating")
    private Boolean isRepeating = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Méthodes utilitaires ───────────────────────────
    public boolean isInFirstYear() {
        return niveau == Niveau.S1 || niveau == Niveau.S2;
    }

    public boolean isInFinalYear() {
        return niveau == Niveau.S5 || niveau == Niveau.S6;
    }

    public enum Niveau {
        S1, S2, S3, S4, S5, S6
    }
}