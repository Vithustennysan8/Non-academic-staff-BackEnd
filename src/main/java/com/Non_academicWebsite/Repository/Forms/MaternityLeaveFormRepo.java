package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.MaternityLeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface MaternityLeaveFormRepo extends JpaRepository<MaternityLeaveForm, Long> {
    List<MaternityLeaveForm> findByUserId(String user);
    List<MaternityLeaveForm> findByUserIdStartingWith(String prefix);
    @Query("SELECT n FROM MaternityLeaveForm n JOIN n.user u WHERE u.faculty = :faculty")
    List<MaternityLeaveForm> findByFaculty(@Param("faculty") String faculty);
    @Query("SELECT n FROM MaternityLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MaternityLeaveForm> findByFacultyAndDepartment(@Param("faculty") String faculty, @Param("department") String department);

    List<MaternityLeaveForm> findByUserIdStartingWithAndHeadStatus(String prefix, String accepted);
    List<MaternityLeaveForm> findByUserIdStartingWithAndDeanStatus(String prefix, String accepted);
    List<MaternityLeaveForm> findByUserIdStartingWithAndCmoStatus(String prefix, String accepted);
    List<MaternityLeaveForm> findByUserIdStartingWithAndRegistrarStatus(String prefix, String accepted);
    @Query("SELECT n FROM MaternityLeaveForm n JOIN n.user u WHERE u.faculty = :faculty AND u.department = :department")
    List<MaternityLeaveForm> findByDepartment(@Param("faculty") String faculty,@Param("department") String department);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    List<MaternityLeaveForm> findByDeanStatus(String accepted);

    List<MaternityLeaveForm> findByCmoStatus(String accepted);

    List<MaternityLeaveForm> findByRegistrarStatus(String accepted);
}
