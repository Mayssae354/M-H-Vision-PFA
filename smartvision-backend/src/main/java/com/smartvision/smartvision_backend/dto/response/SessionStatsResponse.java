package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatsResponse {

    private Long sessionId;
    private String subjectName;
    private String groupName;
    private String room;

    // Compteurs
    private int totalMembers;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private int justifiedCount;

    // Taux de présence en %
    private double attendanceRate;

    // Statut de la session
    private String sessionStatus;
    private String startTime;
    private String endTime;

    // Ou, plus flexible
    private Map<String, Integer> statusCounts; // clé = statut, valeur = nombre
}