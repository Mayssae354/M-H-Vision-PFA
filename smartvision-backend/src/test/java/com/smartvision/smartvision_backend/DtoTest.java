package com.smartvision.smartvision_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartvision.smartvision_backend.dto.response.OverlayDataResponse;
import com.smartvision.smartvision_backend.dto.response.OverlayDataResponse.DetectedStudentInfo;
import com.smartvision.smartvision_backend.dto.response.StudentInfoResponse;
//import com.smartvision.smartvision_backend.entity.global.User.Gender;
//import com.smartvision.smartvision_backend.entity.global.UserOrganization.Role;
//import com.smartvision.smartvision_backend.entity.global.UserOrganization.Status;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.AttendanceStatus;
import com.smartvision.smartvision_backend.entity.tenant.Attendance.DetectionMethod;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import com.smartvision.smartvision_backend.entity.tenant.Session.SessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DTO Tests")
class DtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ═══════════════════════════════════════════════════
    // StudentInfoResponse Tests
    // ═══════════════════════════════════════════════════

    @Nested
    @DisplayName("StudentInfoResponse")
    class StudentInfoResponseTests {

        @Test
        @DisplayName("should build StudentInfoResponse with all fields")
        void shouldBuildStudentInfoResponseCorrectly() {
            StudentInfoResponse response = StudentInfoResponse.builder()
                    .cin("AB123456")
                    .firstName("Mayssae")
                    .lastName("Azirar")
                    .email("m.azirar@esisa.ac.ma")
                    .phone("0612345678")
                    .birthDate(LocalDate.of(2005, 11, 8))
                    //.gender(Gender.FEMALE)
                    .profilePhoto("photo.jpg")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .orgId(1L)
                    .orgName("ESISA - FES")
                    //.role(Role.STUDENT)
                    //.memberStatus(Status.ACTIVE)
                    .joinedAt(ZonedDateTime.now())
                    .groupId(1L)
                    .groupName("Groupe B")
                    .groupCode("GRP-B")
                    .academicYear("2025-2026")
                    .hasFaceEncoding(false)
                    .totalSessions(20)
                    .presentCount(15)
                    .absentCount(3)
                    .lateCount(2)
                    .attendanceRate(85.0)
                    .build();

            assertThat(response.getCin()).isEqualTo("AB123456");
            assertThat(response.getFirstName()).isEqualTo("Mayssae");
            assertThat(response.getLastName()).isEqualTo("Azirar");
            //assertThat(response.getGender()).isEqualTo(Gender.FEMALE);
            //assertThat(response.getRole()).isEqualTo(Role.STUDENT);
            //assertThat(response.getMemberStatus()).isEqualTo(Status.ACTIVE);
            assertThat(response.getAttendanceRate()).isEqualTo(85.0);
            assertThat(response.isHasFaceEncoding()).isFalse();
            assertThat(response.getTotalSessions()).isEqualTo(20);
            assertThat(response.getPresentCount()).isEqualTo(15);
        }

        @Test
        @DisplayName("should create empty StudentInfoResponse")
        void shouldCreateEmptyStudentInfoResponse() {
            StudentInfoResponse response = new StudentInfoResponse();
            assertThat(response).isNotNull();
            assertThat(response.getCin()).isNull();
            assertThat(response.getEmail()).isNull();
        }

        @Test
        @DisplayName("should serialize StudentInfoResponse to JSON")
        void shouldSerializeToJson() throws Exception {
            StudentInfoResponse response = StudentInfoResponse.builder()
                    .cin("AB123456")
                    .firstName("Mayssae")
                    .lastName("Azirar")
                    .email("m.azirar@esisa.ac.ma")
                    //.gender(Gender.FEMALE)
                    //.role(Role.STUDENT)
                    //.memberStatus(Status.ACTIVE)
                    .birthDate(LocalDate.of(2005, 11, 8))
                    .joinedAt(ZonedDateTime.now())
                    .attendanceRate(85.0)
                    .build();

            String json = objectMapper.writeValueAsString(response);

            assertThat(json).contains("AB123456");
            //assertThat(json).contains("FEMALE");
            assertThat(json).contains("STUDENT");
            assertThat(json).contains("ACTIVE");
            assertThat(json).contains("85.0");
        }

        @Test
        @DisplayName("should deserialize JSON to StudentInfoResponse")
        void shouldDeserializeFromJson() throws Exception {
            String json = """
                    {
                        "cin": "AB123456",
                        "firstName": "Mayssae",
                        "lastName": "Azirar",
                        "email": "m.azirar@esisa.ac.ma",
                        "role": "STUDENT",
                        "memberStatus": "ACTIVE",
                        "attendanceRate": 85.0,
                        "hasFaceEncoding": false
                    }
                    """;

            StudentInfoResponse response = objectMapper.readValue(json, StudentInfoResponse.class);

            assertThat(response.getCin()).isEqualTo("AB123456");
            //assertThat(response.getGender()).isEqualTo(Gender.FEMALE);
           // assertThat(response.getRole()).isEqualTo(Role.STUDENT);
           // assertThat(response.getMemberStatus()).isEqualTo(Status.ACTIVE);
            assertThat(response.getAttendanceRate()).isEqualTo(85.0);
            assertThat(response.isHasFaceEncoding()).isFalse();
        }
    }

    // ═══════════════════════════════════════════════════
    // OverlayDataResponse Tests
    // ═══════════════════════════════════════════════════

    @Nested
    @DisplayName("OverlayDataResponse")
    class OverlayDataResponseTests {

        @Test
        @DisplayName("should build OverlayDataResponse with detected students")
        void shouldBuildOverlayDataWithDetectedStudents() {
            DetectedStudentInfo student1 = DetectedStudentInfo.builder()
                    .cin("AB123456")
                    .firstName("Mayssae")
                    .lastName("Azirar")
                    .profilePhoto("photo.jpg")
                    .attendanceStatus(AttendanceStatus.PRESENT)
                    .detectedBy(DetectionMethod.FACE_AI)
                    .confidence(0.97f)
                    .recordedAt(ZonedDateTime.now())
                    .build();

            DetectedStudentInfo student2 = DetectedStudentInfo.builder()
                    .cin("CD789012")
                    .firstName("Sara")
                    .lastName("Idrissi")
                    .attendanceStatus(AttendanceStatus.LATE)
                    .detectedBy(DetectionMethod.MANUAL)
                    .confidence(null)
                    .recordedAt(ZonedDateTime.now())
                    .build();

            OverlayDataResponse overlay = OverlayDataResponse.builder()
                    .sessionId(1L)
                    .sessionStatus(SessionStatus.ACTIVE)
                    .startTime(ZonedDateTime.now())
                    .endTime(ZonedDateTime.now().plusHours(2))
                    .room("Salle A1")
                    .subjectName("Développement Web")
                    .subjectCode("DW301")
                    .groupName("Groupe A")
                    .groupCode("GRP-A")
                    .professorCin("PROF001")
                    .totalExpected(30)
                    .detectedCount(2)
                    .detectionRate(6.67)
                    .detectedStudents(List.of(student1, student2))
                    .build();

            assertThat(overlay.getSessionStatus()).isEqualTo(SessionStatus.ACTIVE);
            assertThat(overlay.getRoom()).isEqualTo("Salle A1");
            assertThat(overlay.getTotalExpected()).isEqualTo(30);
            assertThat(overlay.getDetectedCount()).isEqualTo(2);
            assertThat(overlay.getDetectionRate()).isEqualTo(6.67);
            assertThat(overlay.getDetectedStudents()).hasSize(2);
        }

        @Test
        @DisplayName("should verify first detected student fields")
        void shouldVerifyFirstDetectedStudent() {
            DetectedStudentInfo student = DetectedStudentInfo.builder()
                    .cin("AB123456")
                    .firstName("Mayssae")
                    .lastName("Azirar")
                    .attendanceStatus(AttendanceStatus.PRESENT)
                    .detectedBy(DetectionMethod.FACE_AI)
                    .confidence(0.97f)
                    .recordedAt(ZonedDateTime.now())
                    .build();

            OverlayDataResponse overlay = OverlayDataResponse.builder()
                    .sessionId(1L)
                    .sessionStatus(SessionStatus.ACTIVE)
                    .detectedStudents(List.of(student))
                    .build();

            DetectedStudentInfo result = overlay.getDetectedStudents().get(0);
            assertThat(result.getCin()).isEqualTo("AB123456");
            assertThat(result.getAttendanceStatus()).isEqualTo(AttendanceStatus.PRESENT);
            assertThat(result.getDetectedBy()).isEqualTo(DetectionMethod.FACE_AI);
            assertThat(result.getConfidence()).isEqualTo(0.97f);
        }

        @Test
        @DisplayName("should verify second detected student is LATE with MANUAL detection")
        void shouldVerifySecondDetectedStudent() {
            DetectedStudentInfo student = DetectedStudentInfo.builder()
                    .cin("CD789012")
                    .firstName("Sara")
                    .lastName("Idrissi")
                    .attendanceStatus(AttendanceStatus.LATE)
                    .detectedBy(DetectionMethod.MANUAL)
                    .confidence(null)
                    .recordedAt(ZonedDateTime.now())
                    .build();

            OverlayDataResponse overlay = OverlayDataResponse.builder()
                    .sessionId(1L)
                    .sessionStatus(SessionStatus.ACTIVE)
                    .detectedStudents(List.of(student))
                    .build();

            DetectedStudentInfo result = overlay.getDetectedStudents().get(0);
            assertThat(result.getAttendanceStatus()).isEqualTo(AttendanceStatus.LATE);
            assertThat(result.getDetectedBy()).isEqualTo(DetectionMethod.MANUAL);
            assertThat(result.getConfidence()).isNull();
        }

        @Test
        @DisplayName("should build OverlayDataResponse with empty student list")
        void shouldBuildOverlayWithEmptyStudentList() {
            OverlayDataResponse overlay = OverlayDataResponse.builder()
                    .sessionId(1L)
                    .sessionStatus(SessionStatus.PLANNED)
                    .totalExpected(30)
                    .detectedCount(0)
                    .detectionRate(0.0)
                    .detectedStudents(List.of())
                    .build();

            assertThat(overlay.getDetectedStudents()).isEmpty();
            assertThat(overlay.getDetectedCount()).isZero();
            assertThat(overlay.getDetectionRate()).isZero();
        }

        @Test
        @DisplayName("should serialize OverlayDataResponse to JSON")
        void shouldSerializeOverlayToJson() throws Exception {
            OverlayDataResponse overlay = OverlayDataResponse.builder()
                    .sessionId(1L)
                    .sessionStatus(SessionStatus.ACTIVE)
                    .subjectName("Développement Web")
                    .subjectCode("DW301")
                    .professorCin("PROF001")
                    .totalExpected(30)
                    .detectedCount(10)
                    .detectionRate(33.33)
                    .detectedStudents(List.of())
                    .build();

            String json = objectMapper.writeValueAsString(overlay);

            assertThat(json).contains("ACTIVE");
            assertThat(json).contains("DW301");
            assertThat(json).contains("PROF001");
            assertThat(json).contains("33.33");
        }
    }
}