package com.smartvision.smartvision_backend.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "groups", schema = "univ_esisa_fes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(length = 50)
    private String type;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Column
    private Integer capacity;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy="group")
    private List<Subject> subjects;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}