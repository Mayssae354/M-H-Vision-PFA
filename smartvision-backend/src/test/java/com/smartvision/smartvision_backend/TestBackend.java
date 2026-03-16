package com.smartvision.smartvision_backend;

import com.smartvision.smartvision_backend.entity.global.Organization;
import com.smartvision.smartvision_backend.entity.global.Organization.OrganizationType;

import com.smartvision.smartvision_backend.entity.tenant.Group;
import com.smartvision.smartvision_backend.entity.tenant.Session;
import com.smartvision.smartvision_backend.entity.tenant.Subject;
import com.smartvision.smartvision_backend.repository.global.OrganizationRepository;
import com.smartvision.smartvision_backend.repository.tenant.SessionRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartvision.smartvision_backend.repository.tenant.GroupRepository;
import com.smartvision.smartvision_backend.repository.tenant.SubjectRepository;
import com.smartvision.smartvision_backend.repository.tenant.AttendanceRepository;
import com.smartvision.smartvision_backend.repository.tenant.AbsenceContestRepository;
import com.smartvision.smartvision_backend.repository.tenant.OrgConfigRepository;
import com.smartvision.smartvision_backend.entity.tenant.Attendance;
import com.smartvision.smartvision_backend.entity.tenant.AbsenceContest;
import com.smartvision.smartvision_backend.entity.tenant.OrgConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

@DataJpaTest
@ActiveProfiles("test")
class TestBackend {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AbsenceContestRepository absenceContestRepository;

    @Autowired
    private OrgConfigRepository orgConfigRepository;

    @Test
    void shouldSaveOrganization() {
        Organization org = new Organization();
        org.setName("ESISA");
        org.setType(OrganizationType.UNIVERSITY); // plus simple
        org.setDomainEmail("esisa.ma");
        org.setSchemaName("univ_esisa_fes");

        Organization saved = organizationRepository.save(org);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldCreateSessionAndAttendanceAndContest() {
        Group group = new Group();
        group.setName("GI2");
        group.setCode("GI2-CODE");
        group = groupRepository.save(group);

        Subject subject = new Subject();
        subject.setName("AI");
        subject.setCode("AI-CODE");
        subject.setProfessorCin("12345678");
        subject = subjectRepository.save(subject);

        Session session = new Session();
        session.setGroup(group);
        session.setSubject(subject);
        session.setProfessorCin("12345678");
        session.setRoom("Room A");
        session.setStartTime(ZonedDateTime.now());
        session.setEndTime(ZonedDateTime.now().plusHours(2));

        Session savedSession = sessionRepository.save(session);
        assertThat(savedSession.getId()).isNotNull();

        Attendance attendance = new Attendance();
        attendance.setSession(savedSession);
        attendance.setMemberCin("MEMBER-123");
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        assertThat(savedAttendance.getId()).isNotNull();

        AbsenceContest contest = new AbsenceContest();
        contest.setAttendance(savedAttendance);
        contest.setMemberCin("MEMBER-123");
        contest.setReason("Was sick");

        AbsenceContest savedContest = absenceContestRepository.save(contest);
        assertThat(savedContest.getId()).isNotNull();
    }

    @Test
    void shouldSaveOrgConfig() {
        OrgConfig config = new OrgConfig();
        config.setConfigKey("THEME_COLOR");
        config.setConfigValue("#FFFFFF");
        config.setDescription("Main theme color");

        OrgConfig savedConfig = orgConfigRepository.save(config);
        assertThat(savedConfig.getId()).isNotNull();
    }
}