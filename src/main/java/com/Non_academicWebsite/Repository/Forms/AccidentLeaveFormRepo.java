package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface AccidentLeaveFormRepo extends JpaRepository<AccidentLeaveForm, Long> {
    List<AccidentLeaveForm> findByUserId(String user);
    List<AccidentLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<AccidentLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<AccidentLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<AccidentLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<AccidentLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    List<AccidentLeaveForm> findByUserIdStartingWithAndCmoStatus(String prefix, String accepted);
    @Query("SELECT n FROM AccidentLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<AccidentLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    List<AccidentLeaveForm> findByDeanStatus(String accepted);

    List<AccidentLeaveForm> findByCmoStatus(String accepted);
}
