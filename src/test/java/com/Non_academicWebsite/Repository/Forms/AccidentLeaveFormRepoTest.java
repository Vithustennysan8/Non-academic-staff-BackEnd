package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AccidentLeaveFormRepoTest {

    @Autowired
    private AccidentLeaveFormRepo accidentLeaveFormRepo;
    @Autowired
    private UserRepo userRepo;

    @Test
    void test_save_accident_leave_form_with_valid_values() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())
                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())
                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")
                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Pending")
                .headDescription("Pending")
                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Pending")
                .deanReactedAt(new Date())
                .status("Pending")
                .createdAt(new Date())
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Assert that the AccidentLeaveForm instance is saved correctly
        assertNotNull(accidentLeaveForm.getId());
        assertEquals("Accident Leave Form", accidentLeaveForm.getFormType());
        assertEquals("Jaffna", accidentLeaveForm.getAccidentOccurredDuring());
        assertNotNull(accidentLeaveForm.getDateAndTimeOfAccident());
        assertEquals("Jaffna", accidentLeaveForm.getPlaceOFAccident());
        assertEquals("Exam Duty", accidentLeaveForm.getWhilePerformingAnyDuty());
        assertEquals("Accident", accidentLeaveForm.getNatureOfDanger());
        assertEquals("Peter", accidentLeaveForm.getWhoInspectTheAccident());
        assertEquals("Dean", accidentLeaveForm.getWhoInformedAfterAccident());
        assertEquals("Hospital", accidentLeaveForm.getReferralForTreatment());
        assertNotNull(accidentLeaveForm.getDateAndTimeOfReport());
        assertEquals("2 days", accidentLeaveForm.getDurationOfHospitalStay());
        assertEquals("Yes", accidentLeaveForm.getIsPoliceComplaint());
        assertEquals("Yes", accidentLeaveForm.getExpectAccidentCompensation());
        assertEquals("EN_CO_TO_001", accidentLeaveForm.getUser().getId());
        assertNull(accidentLeaveForm.getFile());
        assertEquals("", accidentLeaveForm.getFileType());
        assertEquals("", accidentLeaveForm.getFileName());
        assertEquals("EN_CO_HD_001", accidentLeaveForm.getHead());
        assertEquals("Pending", accidentLeaveForm.getHeadStatus());
        assertEquals("Pending", accidentLeaveForm.getHeadDescription());
        assertNotNull(accidentLeaveForm.getHeadReactedAt());
        assertEquals("EN_CO_HD_002", accidentLeaveForm.getDean());
        assertEquals("Pending", accidentLeaveForm.getDeanStatus());
        assertEquals("Pending", accidentLeaveForm.getDeanDescription());
        assertNotNull(accidentLeaveForm.getDeanReactedAt());
        assertEquals("Pending", accidentLeaveForm.getStatus());
        assertNotNull(accidentLeaveForm.getCreatedAt());

        assertNotNull(accidentLeaveForm);

    }

    @Test
    void test_save_accident_leave_form_with_null_and_empty_values() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("")
                .dateAndTimeOfAccident(null)
                .placeOFAccident(null)
                .whilePerformingAnyDuty("")
                .natureOfDanger(null)
                .whoInspectTheAccident("")
                .whoInformedAfterAccident(null)
                .referralForTreatment("")
                .dateAndTimeOfReport(null)
                .durationOfHospitalStay(null)
                .isPoliceComplaint("")
                .expectAccidentCompensation(null)

                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head(null)
                .headStatus(null)
                .headDescription("")
                .headReactedAt(null)

                .dean(null)
                .deanStatus(null)
                .deanDescription("")
                .deanReactedAt(null)
                .status("Pending")
                .createdAt(null)
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Assert that the AccidentLeaveForm instance is saved correctly
        assertNotNull(accidentLeaveForm.getId());
        assertEquals("Accident Leave Form", accidentLeaveForm.getFormType());
        assertEquals("", accidentLeaveForm.getAccidentOccurredDuring());
        assertNull(accidentLeaveForm.getDateAndTimeOfAccident());
        assertNull(accidentLeaveForm.getPlaceOFAccident());
        assertEquals("", accidentLeaveForm.getWhilePerformingAnyDuty());
        assertNull(accidentLeaveForm.getNatureOfDanger());
        assertEquals("", accidentLeaveForm.getWhoInspectTheAccident());
        assertNull(accidentLeaveForm.getWhoInformedAfterAccident());
        assertEquals("", accidentLeaveForm.getReferralForTreatment());
        assertNull(accidentLeaveForm.getDateAndTimeOfReport());
        assertNull(accidentLeaveForm.getDurationOfHospitalStay());
        assertEquals("", accidentLeaveForm.getIsPoliceComplaint());
        assertNull(accidentLeaveForm.getExpectAccidentCompensation());

        assertNotNull(accidentLeaveForm.getUser());
        assertEquals("EN_CO_TO_001", accidentLeaveForm.getUser().getId());
        assertNull(accidentLeaveForm.getFile());
        assertEquals("", accidentLeaveForm.getFileType());
        assertEquals("", accidentLeaveForm.getFileName());
        assertNull(accidentLeaveForm.getHead());
        assertNull(accidentLeaveForm.getHeadStatus());
        assertEquals("", accidentLeaveForm.getHeadDescription());
        assertNull(accidentLeaveForm.getHeadReactedAt());

        assertNull(accidentLeaveForm.getDean());
        assertNull(accidentLeaveForm.getDeanStatus());
        assertEquals("", accidentLeaveForm.getDeanDescription());
        assertNull(accidentLeaveForm.getDeanReactedAt());
        assertEquals("Pending", accidentLeaveForm.getStatus());
        assertNull(accidentLeaveForm.getCreatedAt());

        assertNotNull(accidentLeaveForm);

    }

    @Test
    void test_save_accident_leave_form_with_null_user() {
        // Create a AccidentLeaveForm instance with null user
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())

                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())

                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")

                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Pending")
                .headDescription("Pending")

                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Pending")
                .deanReactedAt(new Date())
                .status("Pending")

                .createdAt(new Date())
                .build();

        // When Save the AccidentLeaveForm instance to the database it will throw an error, user
        // should not be null
        assertThrows(DataIntegrityViolationException.class, () -> accidentLeaveFormRepo.save(accidentLeaveForm));

    }
    @Test
    void findByUserIdStartingWith() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())
                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())
                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")
                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Pending")
                .headDescription("Pending")
                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Pending")
                .deanReactedAt(new Date())
                .status("Pending")
                .createdAt(new Date())
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Test the findByUserIdStartingWith method
        List<AccidentLeaveForm> accidentLeaveForms = accidentLeaveFormRepo.findByUserIdStartingWith("EN_CO_TO");
        assertNotEquals(0, accidentLeaveForms.size());
        assertEquals(1, accidentLeaveForms.size());
    }

    @Test
    void test_findByUserId_StartingWith_wrong_prefix() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())
                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())
                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")
                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Pending")
                .headDescription("Pending")
                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Pending")
                .deanReactedAt(new Date())
                .status("Pending")
                .createdAt(new Date())
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Test the findByUserIdStartingWith method
        List<AccidentLeaveForm> accidentLeaveForms = accidentLeaveFormRepo.findByUserIdStartingWith("EN_CO_MA");
        assertNotEquals(1, accidentLeaveForms.size());
        assertEquals(0, accidentLeaveForms.size());

    }

    @Test
    void test_findByUserId_StartingWith_And_ApproverOneStatus_Accepted() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())
                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())
                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")
                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Accepted")
                .headDescription("Okay")
                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Pending")
                .deanReactedAt(new Date())
                .status("Pending")
                .createdAt(new Date())
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Test the findByUserIdStartingWithAndApproverOneStatus method with accepted status
        List<AccidentLeaveForm> accidentLeaveForms = accidentLeaveFormRepo.findByUserIdStartingWithAndHeadStatus("EN_CO_TO", "Accepted");
        assertNotEquals(0, accidentLeaveForms.size());
        assertEquals(1, accidentLeaveForms.size());

    }

    @Test
    void test_findByUserId_StartingWith_And_ApproverOneStatus_Rejected_Or_Pending() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a AccidentLeaveForm instance
        AccidentLeaveForm accidentLeaveForm = AccidentLeaveForm.builder()
                .formType("Accident Leave Form")
                .accidentOccurredDuring("Jaffna")
                .dateAndTimeOfAccident(new Date())
                .placeOFAccident("Jaffna")
                .whilePerformingAnyDuty("Exam Duty")
                .natureOfDanger("Accident")
                .whoInspectTheAccident("Peter")
                .whoInformedAfterAccident("Dean")
                .referralForTreatment("Hospital")
                .dateAndTimeOfReport(new Date())
                .durationOfHospitalStay("2 days")
                .isPoliceComplaint("Yes")
                .expectAccidentCompensation("Yes")
                .user(user)
                .file(null)
                .fileType("")
                .fileName("")
                .head("EN_CO_HD_001")
                .headStatus("Rejected")
                .headDescription("Okay")
                .headReactedAt(new Date())
                .dean("EN_CO_HD_002")
                .deanStatus("Pending")
                .deanDescription("Rejected")
                .deanReactedAt(new Date())
                .status("Pending")
                .createdAt(new Date())
                .build();

        // Save the AccidentLeaveForm instance to the database
        accidentLeaveFormRepo.save(accidentLeaveForm);

        // Test the findByUserIdStartingWithAndApproverOneStatus method with accepted status
        List<AccidentLeaveForm> accidentLeaveForms = accidentLeaveFormRepo.findByUserIdStartingWithAndHeadStatus("EN_CO_TO", "Accepted");
        assertEquals(0, accidentLeaveForms.size());

    }

}