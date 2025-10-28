package com.Non_academicWebsite.Repository.Forms;


import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DynamicFormUserRepo extends JpaRepository<DynamicFormUser, Long> {
    List<DynamicFormUser> findByUserId(String id);
    List<DynamicFormUser> findByUserIdStartingWith(String prefix);

    @Query("SELECT d FROM DynamicFormUser d JOIN d.user u WHERE d.id = :formId AND u.faculty = :faculty " +
            "AND u.department = :department")
    DynamicFormUser findByIdAndFacultyAndDepartment(@Param("formId") Long formId,@Param("faculty") String faculty,
                                                    @Param("department") String department);

    @Query("SELECT d FROM DynamicFormUser d JOIN d.user u WHERE d.id = :formId AND u.faculty = :faculty")
    DynamicFormUser findByIdAndFaculty(@Param("formId") Long formId, @Param("faculty") String faculty);

    List<DynamicFormUser> findByUserIdOrderByCreatedAtDesc(String id);

    List<DynamicFormUser> findByUserIdOrderByIdDesc(String id);
}
