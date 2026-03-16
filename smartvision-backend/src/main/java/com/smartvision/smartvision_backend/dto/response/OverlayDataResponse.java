package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
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
    private String groupName;
    private String groupCode;

    // Professor (CIN only — user is in global schema)
    private String professorCin;

    // Live counters for overlay display
    private int totalExpected;
    private int detectedCount;
    private double detectionRate; // percentage 0.0 - 100.0

    // Detected students list
    private List<DetectedStudentInfo> detectedStudents;

    // Liste des étudiants avec leur statut temps réel
    private List<StudentInfoResponse> students;

    // Stats live
    private int totalStudents;
    private int presentCount;
    private int absentCount;
    private double attendanceRate;

    // Alerte : étudiants ayant dépassé le seuil d'absences
    private List<String> alertStudentCins;

    private Long groupId;

    // clé = statut (PRESENT / ABSENT / JUSTIFIED), valeur = nombre d’étudiants
    private Map<String, Integer> attendanceSummary;

    // date ou période de l’overlay
    private String period;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetectedStudentInfo {

        // From User (global schema)
        private String cin;
        private String firstName;
        private String lastName;
        private String profilePhoto;

        // From Attendance (tenant schema)
        private AttendanceStatus attendanceStatus;
        private DetectionMethod detectedBy;
        private Float confidence;
        private ZonedDateTime recordedAt;
    }
}