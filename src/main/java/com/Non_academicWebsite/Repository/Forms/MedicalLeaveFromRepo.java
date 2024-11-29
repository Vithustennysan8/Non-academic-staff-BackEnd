package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.MedicalLeaveForm;
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
public interface MedicalLeaveFromRepo extends JpaRepository<MedicalLeaveForm, Long> {
    List<MedicalLeaveForm> findByUserId(String user);
    List<MedicalLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.role = :role")
    List<MedicalLeaveForm> findByFacultyAndRole(@Param("faculty") String faculty, @Param("role") Role role);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<MedicalLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :status")
    List<MedicalLeaveForm> findByFacultyAndHeadStatus(@Param("faculty") String faculty, @Param("status") String status);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MedicalLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<MedicalLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndCmoStatus(String prefix, String accepted);
    List<MedicalLeaveForm> findByUserIdStartingWithAndRegistrarStatus(String prefix, String accepted);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MedicalLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);
    void deleteByUserId(String userId);
    List<MedicalLeaveForm> findByDeanStatus(String accepted);
    List<MedicalLeaveForm> findByCmoStatus(String accepted);
    List<MedicalLeaveForm> findByRegistrarStatus(String accepted);
    boolean existsByUserId(String userId);
    @Query("SELECT n FROM MedicalLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND n.headStatus = :headStatus AND " +
            "n.deanStatus = :deanStatus")
    List<MedicalLeaveForm> findByFacultyAndHeadStatusAndDeanStatus(@Param("faculty") String faculty, @Param("headStatus")
    String accepted, @Param("deanStatus") String pending);
    List<MedicalLeaveForm> findByDeanStatusAndCmoStatus(String accepted, String pending);
    List<MedicalLeaveForm> findByCmoStatusAndRegistrarStatus(String accepted, String pending);

    List<MedicalLeaveForm> findByRegistrarStatusAndNaeStatus(String accepted, String pending);
}
