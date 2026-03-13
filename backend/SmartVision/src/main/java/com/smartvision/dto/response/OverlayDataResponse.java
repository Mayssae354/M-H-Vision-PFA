package com.smartvision.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverlayDataResponse {
    private String studentName;
    private String status;
    private String message;
}
