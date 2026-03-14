package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverlayDataResponse {

    // Infos session en cours
    private Long sessionId;
    private String subjectName;
    private String subjectCode;
    private String groupName;
    private String room;
    private String startTime;
    private String sessionStatus;

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
}