package com.Non_academicWebsite.api.Repo;

import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testSaveUser(){
        User user = User.builder()
                .id("EN_CO_MN_005")
                .app_password(null)
                .image_type( null)
                .image_name(null)
                .image_data(null)
                .verified(false)
                .first_name("John")
                .last_name("Doe")
                .date_of_birth(new Date())
                .gender("male")
                .email("john.doe@example.com")
                .phone_no(1234567890L)
                .password("password")
                .address("123 Main St")
                .city("Anytown")
                .postal_code(12345)
                .ic_no("1234567890ABC")
                .emp_id("EMP001")
                .job_type("developer")
                .department("IT")
                .faculty("Engineering")
                .role(Role.USER)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // Assert that all fields are set correctly
        assertEquals("John", user.getFirst_name());
        assertEquals("Doe", user.getLast_name());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("Anytown", user.getCity());
        assertEquals(12345, user.getPostal_code());
        assertEquals("1234567890ABC", user.getIc_no());
        assertEquals("EMP001", user.getEmp_id());
        assertEquals("developer", user.getJob_type());
        assertEquals("IT", user.getDepartment());
        assertEquals("Engineering", user.getFaculty());
        assertEquals("male", user.getGender());
        assertEquals(1234567890L, user.getPhone_no());
        assertEquals("password", user.getPassword());
        assertNull(user.getApp_password());
        assertNull(user.getImage_data());
        assertNull(user.getImage_type());
        assertNull(user.getImage_name());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());


        User savedUser = userRepo.save(user);
        assertNotNull(savedUser);
        assertEquals(userRepo.findAll().size(), 1);
    }

    @Test
    public void testCreateUserEntity_NullEmptyValues() {
        // Create a User entity instance with null or empty values
        User user = User.builder()
                .first_name(null)
                .last_name("")
                .date_of_birth(null)
                .gender("")
                .email("john.doe@example.com")
                .phone_no(null)
                .password("")
                .address(null)
                .city("")
                .postal_code(null)
                .ic_no("")
                .emp_id("")
                .job_type("")
                .department("")
                .faculty("")
                .role(Role.USER)
                .createdAt(null)
                .updatedAt(null)
                .id("EN_CO_MN_005")
                .app_password(null)
                .image_type( null)
                .image_name(null)
                .image_data(null)
                .verified(false)
                .build();

        // Assert that null or empty values are handled appropriately
        assertNull(user.getFirst_name());
        assertEquals("", user.getLast_name());
        assertNull(user.getDate_of_birth());
        assertEquals("", user.getGender());
        assertEquals("john.doe@example.com", user.getEmail());
        assertNull(user.getPhone_no());
        assertEquals("", user.getPassword());
        assertNull(user.getAddress());
        assertEquals("", user.getCity());
        assertNull(user.getPostal_code());
        assertEquals("", user.getIc_no());
        assertEquals("", user.getEmp_id());
        assertEquals("", user.getJob_type());
        assertEquals("", user.getDepartment());
        assertEquals("", user.getFaculty());
        assertEquals(Role.USER, user.getRole());
        assertNull(user.getApp_password());
        assertNull(user.getImage_data());
        assertNull(user.getImage_type());
        assertNull(user.getImage_name());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void testCreateUserEntity_BoundaryValues() {
        // Create a User entity instance with boundary values
        User user = User.builder()
                .first_name("John")
                .last_name("Doe")
                .date_of_birth(new Date())
                .gender("male")
                .email("john.doe@example.com")
                .phone_no(Long.MAX_VALUE) // Maximum value for Long
                .password("password")
                .address("123 Main St")
                .city("Anytown")
                .postal_code(Integer.MAX_VALUE) // Maximum value for Integer
                .ic_no("1234567890ABC")
                .emp_id("EMP001")
                .job_type("developer")
                .department("IT")
                .faculty("Engineering")
                .role(Role.USER)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // Assert that boundary values are handled correctly
        assertEquals(Long.MAX_VALUE, user.getPhone_no());
        assertEquals(Integer.MAX_VALUE, user.getPostal_code());
    }


    @Test
    public void testCreateUserEntity_DateFormats() {
        // Create a User entity instance with specific dates
        Date birthDate = new Date();
        Date createdDate = new Date();
        Date updatedDate = new Date();

        User user = User.builder()
                .email("john.doe@example.com")
                .password("password")
                .date_of_birth(birthDate)
                .createdAt(createdDate)
                .updatedAt(updatedDate)
                .role(Role.USER)
                .build();

        // Assert that date formats are as expected
        assertEquals(birthDate, user.getDate_of_birth());
        assertEquals(createdDate, user.getCreatedAt());
        assertEquals(updatedDate, user.getUpdatedAt());
    }


}
