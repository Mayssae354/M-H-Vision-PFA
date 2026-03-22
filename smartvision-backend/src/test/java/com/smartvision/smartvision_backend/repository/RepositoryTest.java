// src/test/java/com/smartvision/smartvision_backend/repository/RepositoryTest.java

package com.smartvision.smartvision_backend.repository;

import com.smartvision.smartvision_backend.entity.global.*;
import com.smartvision.smartvision_backend.entity.tenant.*;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.DetectionMethod;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest.ContestStatus;
import com.smartvision.smartvision_backend.repository.global.*;
import com.smartvision.smartvision_backend.repository.tenant.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Phase 2 — Repositories")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositoryTest {

    @Autowired OrganizationRepository  organizationRepo;
    @Autowired GroupRepository         groupRepo;
    @Autowired SubjectRepository       subjectRepo;
    @Autowired SessionRepository       sessionRepo;
    @Autowired AttendanceRepository    attendanceRepo;
    @Autowired AbsenceContestRepository contestRepo;

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private Organization savedOrg;
    private Group        savedGroup;
    private Subject      savedSubject;
    private Session      savedSession;
    private Attendance   savedAttendance;

    @BeforeEach
    void setUp() {
        savedOrg = organizationRepo.save(Organization.builder()
                .name("ESISA")
                .type(Organization.OrganizationType.UNIVERSITY)
                .domainEmail("esisa.ac.ma")
                .schemaName("univ_esisa_fes")
                .isActive(true)
                .build());

        savedGroup = groupRepo.save(Group.builder()
                .name("GI3")
                .code("GI3-2024")
                .academicYear("2024-2025")
                .build());

        savedSubject = subjectRepo.save(Subject.builder()
                .name("Intelligence Artificielle")
                .code("IA-301")
                .credits(4)
                .hoursTotal(40)
                .group(savedGroup)
                .professorCin("P123456")
                .semester("S5")
                .build());

        savedSession = sessionRepo.save(Session.builder()
                .subject(savedSubject)
                .group(savedGroup)
                .professorCin("P123456")
                .room("Salle A1")
                .startTime(ZonedDateTime.now())
                .endTime(ZonedDateTime.now().plusHours(2))
                .status(SessionStatus.ACTIVE)
                .build());

        savedAttendance = attendanceRepo.save(Attendance.builder()
                .session(savedSession)
                .memberCin("ETU001")
                .status(AttendanceStatus.ABSENT)
                .detectedBy(DetectionMethod.MANUAL)
                .build());
    }

    // ── OrganizationRepository ────────────────────────────────────────────────

    @Nested
    @DisplayName("OrganizationRepository")
    class OrganizationRepositoryTests {

        @Test
        @DisplayName("findBySchemaName retrouve l'organisation")
        void findBySchemaName() {
            Optional<Organization> result = organizationRepo.findBySchemaName("univ_esisa_fes");
            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("ESISA");
        }

        @Test
        @DisplayName("findByIsActiveTrue retourne les organisations actives")
        void findByIsActiveTrue() {
            List<Organization> active = organizationRepo.findByIsActiveTrue();
            assertThat(active).hasSize(1);
        }

        @Test
        @DisplayName("existsBySchemaName retourne true si existant")
        void existsBySchemaName() {
            assertThat(organizationRepo.existsBySchemaName("univ_esisa_fes")).isTrue();
            assertThat(organizationRepo.existsBySchemaName("inexistant")).isFalse();
        }

        @Test
        @DisplayName("findByType retourne les orgs du bon type")
        void findByType() {
            List<Organization> universities =
                    organizationRepo.findByType(Organization.OrganizationType.UNIVERSITY);
            assertThat(universities).hasSize(1);
        }
    }

    // ── GroupRepository ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("GroupRepository")
    class GroupRepositoryTests {

        @Test
        @DisplayName("findByCode retrouve le groupe")
        void findByCode() {
            Optional<Group> result = groupRepo.findByCode("GI3-2024");
            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("GI3");
        }

        @Test
        @DisplayName("findByAcademicYear retrouve les groupes")
        void findByAcademicYear() {
            List<Group> groups = groupRepo.findByAcademicYear("2024-2025");
            assertThat(groups).hasSize(1);
        }

        @Test
        @DisplayName("existsByCode fonctionne correctement")
        void existsByCode() {
            assertThat(groupRepo.existsByCode("GI3-2024")).isTrue();
            assertThat(groupRepo.existsByCode("INEXISTANT")).isFalse();
        }
    }

    // ── SubjectRepository ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("SubjectRepository")
    class SubjectRepositoryTests {

        @Test
        @DisplayName("findByCode retrouve la matière")
        void findByCode() {
            Optional<Subject> result = subjectRepo.findByCode("IA-301");
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("findByGroupId retourne les matières du groupe")
        void findByGroupId() {
            List<Subject> subjects = subjectRepo.findByGroupId(savedGroup.getId());
            assertThat(subjects).hasSize(1);
        }

        @Test
        @DisplayName("findByProfessorCin retourne les matières du prof")
        void findByProfessorCin() {
            List<Subject> subjects = subjectRepo.findByProfessorCin("P123456");
            assertThat(subjects).hasSize(1);
        }
    }

    // ── SessionRepository ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("SessionRepository")
    class SessionRepositoryTests {

        @Test
        @DisplayName("findByProfessorCinAndStatus retrouve la session active")
        void findByProfessorCinAndStatus() {
            Optional<Session> result = sessionRepo
                    .findByProfessorCinAndStatus("P123456", SessionStatus.ACTIVE);
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("findByGroupId retourne les sessions du groupe")
        void findByGroupId() {
            List<Session> sessions = sessionRepo.findByGroupId(savedGroup.getId());
            assertThat(sessions).hasSize(1);
        }

        @Test
        @DisplayName("findByStartTimeBetween retourne les sessions dans l'intervalle")
        void findByStartTimeBetween() {
            List<Session> sessions = sessionRepo.findByStartTimeBetween(
                    ZonedDateTime.now().minusHours(1),
                    ZonedDateTime.now().plusHours(1));
            assertThat(sessions).hasSize(1);
        }

        @Test
        @DisplayName("countBySubjectId compte les sessions")
        void countBySubjectId() {
            long count = sessionRepo.countBySubjectId(savedSubject.getId());
            assertThat(count).isEqualTo(1);
        }
    }

    // ── AttendanceRepository ──────────────────────────────────────────────────

    @Nested
    @DisplayName("AttendanceRepository")
    class AttendanceRepositoryTests {

        @Test
        @DisplayName("findBySessionIdAndMemberCin retrouve la présence")
        void findBySessionIdAndMemberCin() {
            Optional<Attendance> result = attendanceRepo
                    .findBySessionIdAndMemberCin(savedSession.getId(), "ETU001");
            assertThat(result).isPresent();
            assertThat(result.get().getStatus()).isEqualTo(AttendanceStatus.ABSENT);
        }

        @Test
        @DisplayName("findBySessionId retourne toutes les présences de la session")
        void findBySessionId() {
            List<Attendance> list = attendanceRepo.findBySessionId(savedSession.getId());
            assertThat(list).hasSize(1);
        }

        @Test
        @DisplayName("countByMemberCinAndStatus compte les absences")
        void countByMemberCinAndStatus() {
            long count = attendanceRepo.countByMemberCinAndStatus("ETU001", AttendanceStatus.ABSENT);
            assertThat(count).isEqualTo(1);
        }

        @Test
        @DisplayName("findAbsencesByMemberAndSubject retourne les absences")
        void findAbsencesByMemberAndSubject() {
            List<Attendance> absences = attendanceRepo
                    .findAbsencesByMemberAndSubject("ETU001", savedSubject.getId());
            assertThat(absences).hasSize(1);
        }

        @Test
        @DisplayName("countByStatusForSession retourne les stats")
        void countByStatusForSession() {
            List<Object[]> stats = attendanceRepo.countByStatusForSession(savedSession.getId());
            assertThat(stats).isNotEmpty();
        }
    }

    // ── AbsenceContestRepository ──────────────────────────────────────────────

    @Nested
    @DisplayName("AbsenceContestRepository")
    class AbsenceContestRepositoryTests {

        @Test
        @DisplayName("findByMemberCin retourne les contestations")
        void findByMemberCin() {
            AbsenceContest contest = contestRepo.save(AbsenceContest.builder()
                    .attendance(savedAttendance)
                    .memberCin("ETU001")
                    .reason("Maladie")
                    .status(ContestStatus.PENDING)
                    .build());

            List<AbsenceContest> contests = contestRepo.findByMemberCin("ETU001");
            assertThat(contests).hasSize(1);
        }

        @Test
        @DisplayName("existsByAttendanceId retourne true si contestation existe")
        void existsByAttendanceId() {
            contestRepo.save(AbsenceContest.builder()
                    .attendance(savedAttendance)
                    .memberCin("ETU001")
                    .reason("Maladie")
                    .status(ContestStatus.PENDING)
                    .build());

            assertThat(contestRepo.existsByAttendanceId(savedAttendance.getId())).isTrue();
        }

        @Test
        @DisplayName("findByStatus retourne les contestations PENDING")
        void findByStatus() {
            contestRepo.save(AbsenceContest.builder()
                    .attendance(savedAttendance)
                    .memberCin("ETU001")
                    .reason("Maladie")
                    .status(ContestStatus.PENDING)
                    .build());

            List<AbsenceContest> pending = contestRepo.findByStatus(ContestStatus.PENDING);
            assertThat(pending).hasSize(1);
        }
    }
}