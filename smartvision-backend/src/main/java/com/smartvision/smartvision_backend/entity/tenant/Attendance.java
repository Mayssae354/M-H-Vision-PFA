package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "attendance",schema = "univ_esisa_fes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "member_cin"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // String car users est dans le schema global
    @Column(name = "member_cin", nullable = false, length = 20)
    private String memberCin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "detected_by", nullable = false, length = 20)
    @Builder.Default
    private DetectionMethod detectedBy = DetectionMethod.MANUAL;

    // Score de confiance de la reconnaissance faciale (0.0 - 1.0)
    @Column
    private Float confidence;

    @Column(name = "recorded_at")
    private ZonedDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        recordedAt = ZonedDateTime.now();
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, JUSTIFIED
    }

    public enum DetectionMethod {
        FACE_AI, MANUAL, VOICE
    }
}