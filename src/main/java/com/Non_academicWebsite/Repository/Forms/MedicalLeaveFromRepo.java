package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface MedicalLeaveFromRepo extends JpaRepository<MedicalLeaveForm, Long> {
    List<MedicalLeaveForm> findByUserId(String user);
    List<MedicalLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<MedicalLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MedicalLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<MedicalLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndCmoStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndRegistrarStatus(String prefix, String accepted);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MedicalLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    List<MedicalLeaveForm> findByDeanStatus(String accepted);

    List<MedicalLeaveForm> findByCmoStatus(String accepted);

    List<MedicalLeaveForm> findByRegistrarStatus(String accepted);
}
