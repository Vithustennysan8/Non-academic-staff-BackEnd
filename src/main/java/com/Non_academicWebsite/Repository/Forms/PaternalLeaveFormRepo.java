package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.PaternalLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface PaternalLeaveFormRepo extends JpaRepository<PaternalLeaveForm, Long> {
    List<PaternalLeaveForm> findByUserId(String user);
    List<PaternalLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.role = :role")
    List<PaternalLeaveForm> findByFacultyAndRole(@Param("faculty") String faculty, @Param("role") Role role);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<PaternalLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :status")
    List<PaternalLeaveForm> findByFacultyAndHeadStatus(@Param("faculty") String faculty, @Param("status") String status);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<PaternalLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<PaternalLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<PaternalLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<PaternalLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);
    void deleteByUserId(String userId);
    List<PaternalLeaveForm> findByDeanStatus(String accepted);

    boolean existsByUserId(String userId);
    @Query("SELECT n FROM PaternalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :headStatus AND " +
            "n.deanStatus = :deanStatus")
    List<PaternalLeaveForm> findByFacultyAndHeadStatusAndDeanStatus(@Param("faculty") String faculty, @Param("headStatus")
    String accepted, @Param("deanStatus") String pending);

    List<PaternalLeaveForm> findByDeanStatusAndNaeStatus(String accepted, String pending);
}
