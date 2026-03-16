package com.smartvision.smartvision_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.smartvision.smartvision_backend.entity.tenant.StudentProfile.Niveau;
import com.smartvision.smartvision_backend.entity.tenant.ProfessorProfile.Grade;

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

    // ── Champs étudiant (optionnels selon rôle) ────────
    private String filiere;
    private Niveau niveau;
    private String apogeeCode;
    private String academicYear;

    // ── Champs professeur (optionnels selon rôle) ──────
    private String speciality;
    private Grade grade;
    private String office;
}