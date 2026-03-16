package com.smartvision.smartvision_backend.dto.response;

import com.smartvision.smartvision_backend.entity.tenant.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatsResponse {

    private Long sessionId;
    private Session.SessionStatus status;
    private String room;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime createdAt;

    // Subject info
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String semester;

    // Professor (CIN only — user is in global schema)
    private String professorCin;

    // Group info
    private Long groupId;
    private String groupName;
    private String groupCode;
    private String academicYear;

    // Attendance stats
    private int totalStudents;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private int justifiedCount;
    private double attendanceRate;

    // Compteurs
    private int totalMembers;

    // Ou, plus flexible
    private Map<String, Integer> statusCounts; // clé = statut, valeur = nombre
}