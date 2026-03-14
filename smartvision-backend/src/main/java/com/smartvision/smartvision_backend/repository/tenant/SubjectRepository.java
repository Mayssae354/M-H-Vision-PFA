package com.smartvision.smartvision_backend.repository.tenant;

import com.smartvision.smartvision_backend.entity.tenant.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCode(String code);

    // Toutes les matières d'un groupe
    List<Subject> findByGroupId(Long groupId);

    // Toutes les matières d'un professeur
    List<Subject> findByProfessorCin(String professorCin);

    // Matières d'un prof dans un groupe précis
    List<Subject> findByProfessorCinAndGroupId(String professorCin, Long groupId);

    boolean existsByCode(String code);
}