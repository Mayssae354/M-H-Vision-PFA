package com.smartvision.smartvision_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Le CIN est obligatoire")
    private String cin;

    @NotBlank(message = "Le token d'invitation est obligatoire")
    private String inviteToken;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Mot de passe minimum 8 caractères")
    private String password;

    @NotBlank(message = "La confirmation est obligatoire")
    private String confirmPassword;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;
}