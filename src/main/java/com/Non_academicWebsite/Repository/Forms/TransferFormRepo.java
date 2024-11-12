package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface TransferFormRepo extends JpaRepository<TransferForm, Long> {
    List<TransferForm> findByUserId(String user);
    List<TransferForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM TransferForm n JOIN n.user u WHERE u.faculty = :faculty AND u.role = :role")
    List<TransferForm> findByFacultyAndRole(@Param("faculty") String faculty, @Param("role") Role role);
    @Query("SELECT n FROM TransferForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<TransferForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM TransferForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<TransferForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);
    List<TransferForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    @Query("SELECT n FROM TransferForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<TransferForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);
    void deleteByUserId(String userId);
    List<TransferForm> findByDeanStatus(String accepted);
    List<TransferForm> findByRegistrarStatus(String accepted);
    boolean existsByUserId(String userId);

    List<TransferForm> findByNaeStatus(String accepted);
}
