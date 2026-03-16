package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "professor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorProfile {

    @Id
    @Column(name = "professor_cin", length = 20)
    private String professorCin;

    @Column(length = 100)
    private String speciality;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Grade grade;

    @Column(length = 50)
    private String office;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "is_permanent")
    private Boolean isPermanent = true;

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

    public enum Grade {
        ASSISTANT,   // Professeur assistant
        PES,         // Professeur de l'enseignement supérieur
        PH,          // Professeur habilité
        VACATAIRE,   // Vacataire (temps partiel)
        INVITE       // Professeur invité
    }
}
