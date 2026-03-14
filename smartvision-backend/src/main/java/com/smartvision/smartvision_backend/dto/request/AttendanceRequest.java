package com.smartvision.smartvision_backend.dto.request;

import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.DetectionMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRequest {

    @NotNull(message = "L'ID de session est obligatoire")
    private Long sessionId;

    @NotBlank(message = "Le CIN du membre est obligatoire")
    private String memberCin;

    @NotNull(message = "Le statut est obligatoire")
    private AttendanceStatus status;

    @NotNull(message = "La méthode de détection est obligatoire")
    private DetectionMethod detectedBy;

    private String reason;           // Optionnel : justification d’absence

    private String proofUrl;

    // Optionnel — rempli par l'IA de reconnaissance faciale
    private Float confidence;
}