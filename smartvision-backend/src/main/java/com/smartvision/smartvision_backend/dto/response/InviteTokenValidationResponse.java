package com.smartvision.smartvision_backend.dto.response;

import lombok.*;
import com.smartvision.smartvision_backend.entity.global.UserOrganization.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteTokenValidationResponse {
    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String orgName;
    private Long orgId;
    // true = token valide, false = expiré/invalide
    private boolean valid;
    private String message;
}