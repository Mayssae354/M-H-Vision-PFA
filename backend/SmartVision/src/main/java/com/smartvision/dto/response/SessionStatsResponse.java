package com.smartvision.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatsResponse {
    private Long sessionId;
    private int totalStudents;
    private int present;
    private int absent;
}
