package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.AccidentLeaveForm;
import com.Non_academicWebsite.Entity.Forms.NormalLeaveForm;
import com.Non_academicWebsite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface NormalLeaveFormRepo extends JpaRepository<NormalLeaveForm, Long> {
    List<NormalLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM NormalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.role = :role")
    List<NormalLeaveForm> findByFacultyAndRole(@Param("faculty") String faculty, @Param("role") Role role);
    @Query("SELECT n FROM NormalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<NormalLeaveForm> findByDepartment(@Param("faculty") String faculty, @Param("department") String department);
    @Query("SELECT n FROM NormalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<NormalLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM NormalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<NormalLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);
    List<NormalLeaveForm> findByUserId(String id);
    List<NormalLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    void deleteByUserId(String userId);

    boolean existsByUserId(String userId);
}
