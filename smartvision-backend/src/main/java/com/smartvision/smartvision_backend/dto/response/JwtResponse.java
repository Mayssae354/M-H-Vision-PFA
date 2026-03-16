package com.smartvision.smartvision_backend.dto.response;

import lombok.*;

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

    // Info profil selon rôle
    private String filiere;        // étudiant
    private String niveau;         // étudiant
    private String apogeeCode;     // étudiant
    private String speciality;     // professeur
    private String grade;          // professeur
    private String office;         // professeur
}
