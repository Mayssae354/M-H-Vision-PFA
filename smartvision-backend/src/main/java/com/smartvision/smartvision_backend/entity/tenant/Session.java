package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sessions", schema = "univ_esisa_fes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // String car users est dans le schema global
    @Column(name = "professor_cin", nullable = false, length = 20)
    private String professorCin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(length = 50)
    private String room;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SessionStatus status = SessionStatus.PLANNED;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy="session")
    private List<Attendance> attendances;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }

    public enum SessionStatus {
        PLANNED, ACTIVE, CLOSED, CANCELLED
    }
}