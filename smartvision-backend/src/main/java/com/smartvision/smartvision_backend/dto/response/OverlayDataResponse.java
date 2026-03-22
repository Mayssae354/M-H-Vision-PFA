package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.DetectionMethod;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverlayDataResponse {

    // Infos session en cours
    private Long sessionId;
    private SessionStatus sessionStatus;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String room;

    // Subject info
    private String subjectName;
    private String subjectCode;

    // Group info
    private Long groupId;
    private String groupName;
    private String groupCode;

    // Professor (CIN only — user is in global schema)
    private String professorCin;

    // Live counters for overlay display
    private int totalExpected;
    private int detectedCount;
    private double detectionRate;

    // Detected students list (PRESENT + LATE)
    @Builder.Default
    private List<DetectedStudentInfo> detectedStudents = new ArrayList<>();

    // Liste complète des étudiants du groupe avec statut
    @Builder.Default
    private List<StudentInfoResponse> students = new ArrayList<>();

    // Stats live
    private int totalStudents;
    private int presentCount;
    private int absentCount;
    private double attendanceRate;

    // Alerte : étudiants ayant dépassé le seuil d'absences
    @Builder.Default
    private List<String> alertStudentCins = new ArrayList<>();

    // clé = statut (PRESENT / ABSENT / JUSTIFIED), valeur = nombre
    private Map<String, Integer> attendanceSummary;

    // date ou période de l'overlay
    private String period;

    // ── Classe imbriquée ──────────────────────────────────────────────────────

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetectedStudentInfo {

        // Depuis User (global schema)
        private String cin;
        private String firstName;
        private String lastName;
        private String profilePhoto;

        // Depuis Attendance (tenant schema)
        private AttendanceStatus attendanceStatus;
        private DetectionMethod detectedBy;
        private Float confidence;
        private ZonedDateTime recordedAt;
    }
}