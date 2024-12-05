package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DynamicFormRepo extends JpaRepository<DynamicForm,Long> {
    boolean existsByFormTypeAndDepartmentAndFaculty(String formType, String department, String faculty);
    DynamicForm findByFormTypeAndDepartmentAndFaculty(String formType, String department, String faculty);
    List<DynamicForm> findAllByDepartmentAndFaculty(String department, String faculty);
    boolean existsByFormTypeAndDepartmentAndFacultyAndIsAvailable(String form, String department,
                                                                  String faculty, boolean isAvailable);

    DynamicForm findByFormTypeAndDepartmentAndFacultyAndIsAvailable(String formType, String department,
                                                                    String faculty, boolean isAvailable);

    List<DynamicForm> findAllByDepartmentAndFacultyAndIsAvailable(String department, String faculty,
                                                                  boolean isAvailable);

    DynamicForm findByIdAndIsAvailable(Long id, boolean isAvailable);
}
