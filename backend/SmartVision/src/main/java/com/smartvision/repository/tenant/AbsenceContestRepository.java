package com.smartvision.repository.tenant;

import com.smartvision.entity.tenant.AbsenceContest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceContestRepository extends JpaRepository<AbsenceContest, Long> {
}
