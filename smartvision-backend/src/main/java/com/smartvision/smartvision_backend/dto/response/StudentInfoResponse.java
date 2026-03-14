package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoResponse {

    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePhoto;

    // Infos académiques
    private String groupName;
    private String groupCode;

    // Stats absences pour la session/matière courante
    private int totalAbsences;
    private int justifiedAbsences;
    private int remainingAllowed;   // seuil - absences actuelles

    // Statut dans la session courante
    private String currentStatus;   // PRESENT, ABSENT, LATE...
    private Float detectionConfidence;
}