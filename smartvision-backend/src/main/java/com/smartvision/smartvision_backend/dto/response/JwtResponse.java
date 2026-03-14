package com.smartvision.smartvision_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;

    // Info utilisateur
    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long orgId;
    private String orgName;
    private String schema;
}
