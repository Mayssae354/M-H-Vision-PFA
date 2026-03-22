package com.smartvision.smartvision_backend.dto.response;

import com.smartvision.smartvision_backend.entity.global.Organization;
//import com.smartvision.smartvision_backend.entity.global.User.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoResponse {


    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;       // LocalDate (cohérent avec User)
    //private Gender gender;             // enum Gender de User
    private String profilePhoto;
    private Boolean isActive;
    private LocalDateTime createdAt; 

    
    private Long orgId;
    private String orgName;
    private String role;
    private String memberStatus; // ACTIVE, SUSPENDED, PENDING
    private ZonedDateTime joinedAt;
    private ZonedDateTime  suspendedAt;
    
    // Infos académiques
    private Long groupId;
    private String groupName;
    private String groupCode;
    private String academicYear;

    // Face encoding status (from face_encodings table)
    private boolean hasFaceEncoding;

    // Attendance summary (optional, for profile view)
    private Integer totalSessions;
    private Integer presentCount;
    private Integer absentCount;
    private Integer lateCount;
    private Integer justifiedAbsences;
    private Double attendanceRate;

    private int remainingAllowed;   // seuil - absences actuelles
}