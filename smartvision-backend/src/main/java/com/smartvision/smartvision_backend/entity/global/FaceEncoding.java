package com.smartvision.smartvision_backend.entity.global;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "face_encodings", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaceEncoding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cin", nullable = false, unique = true)
    private User user;

    @Column(name = "encoding_vector", nullable = false,
            columnDefinition = "BYTEA")
    private byte[] encodingVector;

    @Column(name = "encoding_hash", length = 64)
    private String encodingHash;

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
    public boolean hasEncoding() {
        return encodingVector != null
                && encodingVector.length > 0;
    }

    public int getVectorSize() {
        return encodingVector != null
                ? encodingVector.length
                : 0;
    }
}