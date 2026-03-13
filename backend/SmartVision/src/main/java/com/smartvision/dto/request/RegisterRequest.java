package com.smartvision.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
