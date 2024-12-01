package com.Non_academicWebsite.Repository.ApprovalFlow;

import aj.org.objectweb.asm.commons.Remapper;
import com.Non_academicWebsite.Entity.ApprovalFlow.ApprovalFlow;
import com.Non_academicWebsite.Entity.Forms.DynamicForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ApprovalFlowRepo extends JpaRepository<ApprovalFlow, Integer> {
    List<ApprovalFlow> findByDepartmentAndFaculty(String department, String faculty);
    boolean existsByDynamicFormIdAndUniqueNameAndDepartmentAndFaculty(Long id, String uniqueName, String department, String faculty);
    void deleteByDynamicFormIdAndUniqueNameAndDepartmentAndFaculty(Long id, String uniqueName, String department, String faculty);
}
