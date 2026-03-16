package com.smartvision.smartvision_backend.entity.global;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_logs", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cin")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false, length = 255)
    private String endpoint;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "response_ms")
    private Integer responseMs;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "is_suspicious")
    private Boolean suspicious = false;

    @Column(name = "attack_type", length = 100)
    private String attackType;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public boolean isFailedRequest() {
        return statusCode >= 400;
    }

    public boolean isCritical() {
        return suspicious != null
                && suspicious
                && attackType != null
                && (attackType.equals("SQL_INJECTION")
                ||  attackType.equals("BRUTE_FORCE")
                ||  attackType.equals("INTER_ORG_ACCESS"));
    }

    public static SecurityLog createLog(
            String ip,
            String endpoint,
            String method,
            Integer statusCode,
            Integer responseMs,
            String userAgent) {
        return SecurityLog.builder()
                .ipAddress(ip)
                .endpoint(endpoint)
                .method(method)
                .statusCode(statusCode)
                .responseMs(responseMs)
                .userAgent(userAgent)
                .suspicious(false)
                .build();
    }
}