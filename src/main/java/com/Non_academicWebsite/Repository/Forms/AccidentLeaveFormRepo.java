package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface AccidentLeaveFormRepo extends JpaRepository<AccidentLeaveForm, Long> {
    List<AccidentLeaveForm> findByUserId(String user);
    List<AccidentLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.role = :role")
    List<AccidentLeaveForm> findByFacultyAndRole(@Param("faculty") String faculty, @Param("role") Role role);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<AccidentLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :status")
    List<AccidentLeaveForm> findByFacultyAndHeadStatus(@Param("faculty") String faculty, @Param("status") String status);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<AccidentLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<AccidentLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<AccidentLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    List<AccidentLeaveForm> findByUserIdStartingWithAndCmoStatus(String prefix, String accepted);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<AccidentLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);
    void deleteByUserId(String userId);
    List<AccidentLeaveForm> findByDeanStatus(String accepted);
    List<AccidentLeaveForm> findByCmoStatus(String accepted);

    boolean existsByUserId(String userId);

    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :headStatus AND " +
            "n.deanStatus = :deanStatus")
    List<AccidentLeaveForm> findByFacultyAndHeadStatusAndDeanStatus(@Param("faculty") String faculty,@Param("headStatus")
    String accepted,@Param("deanStatus") String pending);
    List<AccidentLeaveForm> findByDeanStatusAndCmoStatus(String accepted, String pending);

    List<AccidentLeaveForm> findByCmoStatusAndNaeStatus(String accepted, String pending);
}
