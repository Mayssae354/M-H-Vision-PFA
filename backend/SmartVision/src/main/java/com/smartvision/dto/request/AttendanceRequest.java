package com.smartvision.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private Long sessionId;
    private String memberCin;
    private String detectionMethod; // FACE_AI, MANUAL, VOICE
    private Double confidence;
}
