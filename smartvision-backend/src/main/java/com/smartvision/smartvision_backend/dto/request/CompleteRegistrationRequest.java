package com.smartvision.smartvision_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompleteRegistrationRequest {

    @NotBlank
    private String token;          // UUID du lien d'invitation

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String confirmPassword;
}
