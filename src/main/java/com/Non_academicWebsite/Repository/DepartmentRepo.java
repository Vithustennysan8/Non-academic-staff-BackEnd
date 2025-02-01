package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    List<Department> findAllByFacultyId(Integer faculty);
    boolean existsByFacultyIdAndAlias(Integer id, String alias);

    boolean existsByFacultyIdAndDepartmentName(Integer id, String name);

    Department findByFacultyIdAndDepartmentName(Integer facId, String department);
}
