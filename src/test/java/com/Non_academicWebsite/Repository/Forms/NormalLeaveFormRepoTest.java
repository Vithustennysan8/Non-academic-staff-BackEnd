package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.BeforeAll;
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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class NormalLeaveFormRepoTest {

    @Autowired
    private NormalLeaveFormRepo normalLeaveFormRepo;
    @Autowired
    private UserRepo userRepo;

    @Test
    void test_save_normal_leave_form() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);


        // Create a NormalLeaveForm instance
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo("E12232")
                .designation("Manager")
                .firstAppointmentDate(new Date())
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(30)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(60)
                .user(user)
                .noOfLeaveDays(2)
                .leaveType("casual")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement("Substitute officer")
                .addressDuringTheLeave("Jaffna")
                .approverOne("EN_CO_HD_001")
                .approverOneStatus("Pending")
                .approverOneDescription("Pending")
                .approverOneReactedAt(new Date())
                .status("Pending")
                .build();

        // Save the NormalLeaveForm instance to the database
        normalLeaveFormRepo.save(normalLeaveForm);

        // Assert that the NormalLeaveForm instance is saved correctly
        assertNotNull(normalLeaveForm.getId());
        assertEquals("Normal Leave Form", normalLeaveForm.getFormType());
        assertEquals("E12232", normalLeaveForm.getUpfNo());
        assertEquals("Manager", normalLeaveForm.getDesignation());
        assertNotNull(normalLeaveForm.getFirstAppointmentDate());
        assertEquals(10, normalLeaveForm.getCasualLeaveLastYear());
        assertEquals(20, normalLeaveForm.getVacationLeaveLastYear());
        assertEquals(30, normalLeaveForm.getSickLeaveLastYear());
        assertEquals(40, normalLeaveForm.getCasualLeaveThisYear());
        assertEquals(50, normalLeaveForm.getVacationLeaveThisYear());
        assertEquals(60, normalLeaveForm.getSickLeaveThisYear());
        assertEquals(2, normalLeaveForm.getNoOfLeaveDays());
        assertEquals("casual", normalLeaveForm.getLeaveType());
        assertNotNull(normalLeaveForm.getLeaveAppliedDate());
        assertEquals("Wedding", normalLeaveForm.getReason());
        assertEquals("Substitute officer", normalLeaveForm.getArrangement());
        assertEquals("Jaffna", normalLeaveForm.getAddressDuringTheLeave());
        assertEquals("EN_CO_HD_001", normalLeaveForm.getApproverOne());
        assertEquals("Pending", normalLeaveForm.getApproverOneStatus());
        assertEquals("Pending", normalLeaveForm.getApproverOneDescription());
        assertNotNull(normalLeaveForm.getApproverOneReactedAt());
        assertEquals("Pending", normalLeaveForm.getStatus());
        assertNotNull(normalLeaveForm);

    }

    @Test
    void test_Save_Normal_leave_form_with_null_values() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);

        // Create a NormalLeaveForm instance
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo(null)
                .designation("")
                .firstAppointmentDate(null)
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(null)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(0)
                .user(user)
                .noOfLeaveDays(2)
                .leaveType("")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement(null)
                .addressDuringTheLeave("")
                .approverOne(null)
                .approverOneStatus(null)
                .approverOneDescription("")
                .approverOneReactedAt(null)
                .status("Pending")
                .build();

        // Save the NormalLeaveForm instance to the database
        normalLeaveFormRepo.save(normalLeaveForm);

        // Assert that the NormalLeaveForm instance is saved correctly
        assertNotNull(normalLeaveForm.getId());
        assertEquals("Normal Leave Form", normalLeaveForm.getFormType());
        assertNull(normalLeaveForm.getUpfNo());
        assertEquals("", normalLeaveForm.getDesignation());
        assertNull(normalLeaveForm.getFirstAppointmentDate());
        assertEquals(10, normalLeaveForm.getCasualLeaveLastYear());
        assertEquals(20, normalLeaveForm.getVacationLeaveLastYear());
        assertNull( normalLeaveForm.getSickLeaveLastYear());
        assertEquals(40, normalLeaveForm.getCasualLeaveThisYear());
        assertEquals(50, normalLeaveForm.getVacationLeaveThisYear());
        assertEquals(0, normalLeaveForm.getSickLeaveThisYear());
        assertEquals(2, normalLeaveForm.getNoOfLeaveDays());
        assertEquals("", normalLeaveForm.getLeaveType());
        assertNotNull(normalLeaveForm.getLeaveAppliedDate());
        assertEquals("Wedding", normalLeaveForm.getReason());
        assertNull(normalLeaveForm.getArrangement());
        assertEquals("", normalLeaveForm.getAddressDuringTheLeave());
        assertNull(normalLeaveForm.getApproverOne());
        assertNull(normalLeaveForm.getApproverOneStatus());
        assertEquals("", normalLeaveForm.getApproverOneDescription());
        assertNull(normalLeaveForm.getApproverOneReactedAt());
        assertEquals("Pending", normalLeaveForm.getStatus());
        assertNotNull(normalLeaveForm);
        }

    @Test
    void test_Normal_leave_form_with_NullUser() {
        // Create a NormalLeaveForm instance with null user
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo("E12232")
                .designation("Manager")
                .firstAppointmentDate(new Date())
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(30)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(60)
                .noOfLeaveDays(2)
                .leaveType("casual")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement("Substitute officer")
                .addressDuringTheLeave("Jaffna")
                .approverOne("EN_CO_HD_001")
                .approverOneStatus("Pending")
                .approverOneDescription("Pending")
                .approverOneReactedAt(new Date())
                .status("Pending")
                .build();

        // When Save the NormalLeaveForm instance to the database it will throw an error user
        // should not be null
        assertThrows(DataIntegrityViolationException.class, () -> normalLeaveFormRepo.save(normalLeaveForm));
    }

    @Test
    void test_findByUserIdStartingWith() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);


        // Create a NormalLeaveForm instance
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo("E12232")
                .designation("Manager")
                .firstAppointmentDate(new Date())
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(30)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(60)
                .user(user)
                .noOfLeaveDays(2)
                .leaveType("casual")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement("Substitute officer")
                .addressDuringTheLeave("Jaffna")
                .approverOne("EN_CO_HD_001")
                .approverOneStatus("Pending")
                .approverOneDescription("Pending")
                .approverOneReactedAt(new Date())
                .status("Pending")
                .build();

        // Save the NormalLeaveForm instance to the database
        normalLeaveFormRepo.save(normalLeaveForm);

        // Test the findByUserIdStartingWith method
        List<NormalLeaveForm> normalLeaveForms = normalLeaveFormRepo.findByUserIdStartingWith("EN_CO_TO");
        assertNotEquals(0, normalLeaveForms.size());
        assertEquals(1, normalLeaveForms.size());
    }

    @Test
    void test_findByStartingWith_Null() {

        // Test the findByUserIdStartingWith method withNot not valid prefix
        List<NormalLeaveForm> normalLeaveForms = normalLeaveFormRepo.findByUserIdStartingWith("EN_CO_TO");
        assertEquals(0, normalLeaveForms.size());

    }

    @Test
    void test_findByUserIdStartingWithAndApproverOneStatus_with_accepted() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);


        // Create a NormalLeaveForm instance
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo("E12232")
                .designation("Manager")
                .firstAppointmentDate(new Date())
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(30)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(60)
                .user(user)
                .noOfLeaveDays(2)
                .leaveType("casual")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement("Substitute officer")
                .addressDuringTheLeave("Jaffna")
                .approverOne("EN_CO_HD_001")
                .approverOneStatus("Accepted")
                .approverOneDescription("Okay, get well soon.")
                .approverOneReactedAt(new Date())
                .status("Accepted")
                .build();

        // Save the NormalLeaveForm instance to the database
        normalLeaveFormRepo.save(normalLeaveForm);

        // Test the findByUserIdStartingWithAndApproverOneStatus method with accepted status
        List<NormalLeaveForm> normalLeaveForms = normalLeaveFormRepo.findByUserIdStartingWithAndApproverOneStatus("EN_CO_TO", "Accepted");
        assertNotEquals(0, normalLeaveForms.size());
        assertEquals(1, normalLeaveForms.size());
    }

    @Test
    void test_findByUserIdStartingWithAndApproverOneStatus_with_rejected_or_pending() {
        // Create a new User instance
        User user = User.builder()
                .id("EN_CO_TO_001")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Save the User instance to the database
        userRepo.save(user);


        // Create a NormalLeaveForm instance
        NormalLeaveForm normalLeaveForm = NormalLeaveForm.builder()
                .formType("Normal Leave Form")
                .upfNo("E12232")
                .designation("Manager")
                .firstAppointmentDate(new Date())
                .casualLeaveLastYear(10)
                .vacationLeaveLastYear(20)
                .sickLeaveLastYear(30)
                .casualLeaveThisYear(40)
                .vacationLeaveThisYear(50)
                .sickLeaveThisYear(60)
                .user(user)
                .noOfLeaveDays(2)
                .leaveType("casual")
                .leaveAppliedDate(new Date())
                .reason("Wedding")
                .arrangement("Substitute officer")
                .addressDuringTheLeave("Jaffna")
                .approverOne("EN_CO_HD_001")
                .approverOneStatus("Pending")
                .approverOneDescription("")
                .approverOneReactedAt(new Date())
                .status("Pending")
                .build();

        // Save the NormalLeaveForm instance to the database
        normalLeaveFormRepo.save(normalLeaveForm);

        // Test the findByUserIdStartingWithAndApproverOneStatus method with accepted status
        List<NormalLeaveForm> normalLeaveForms = normalLeaveFormRepo.findByUserIdStartingWithAndApproverOneStatus("EN_CO_TO", "Accepted");
        assertEquals(0, normalLeaveForms.size());
    }

}