package com.smartvision.smartvision_backend.service.integration;

import com.smartvision.smartvision_backend.dto.response.StudentInfoResponse;
import com.smartvision.smartvision_backend.entity.global.AllowedMember;
import com.smartvision.smartvision_backend.entity.global.Organization;
import com.smartvision.smartvision_backend.entity.tenant.Group;
import com.smartvision.smartvision_backend.entity.tenant.Subject;

import java.util.List;

/**
 * Contrat d'intégration avec un système universitaire externe.
 * Deux implémentations : REST API et CSV import.
 */
public interface UniversityDataProvider {

    /**
     * Récupère la liste des membres autorisés (étudiants + profs)
     * pour une organisation donnée.
     */
    List<AllowedMember> fetchAllowedMembers(Organization organization);

    /**
     * Récupère les groupes/promotions de l'organisation.
     */
    List<Group> fetchGroups(Organization organization);

    /**
     * Récupère les matières de l'organisation.
     */
    List<Subject> fetchSubjects(Organization organization);

    /**
     * Vérifie si la source de données est disponible/accessible.
     */
    boolean isAvailable(Organization organization);
    List<StudentInfoResponse> fetchStudents();

    List<String> fetchProfessors();

    List<Group> fetchGroups();

    List<Subject> fetchSubjects();
}