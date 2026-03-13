package com.smartvision.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceCommandRequest {
    private String commandText;
    private Long sessionId;
}
