package com.smartvision.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoResponse {
    private Long id;
    private String cin;
    private String firstName;
    private String lastName;
    private String status;
}
