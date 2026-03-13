package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    @Builder.Default
    private Integer credits = 0;

    @Column(name = "hours_total")
    @Builder.Default
    private Integer hoursTotal = 0;

    // Relation vers Group (même schema tenant → FK normale)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // String car users est dans le schema global
    @Column(name = "professor_cin", nullable = false, length = 20)
    private String professorCin;

    @Column(length = 20)
    private String semester;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}