package com.Non_academicWebsite.Repository.ApprovalFlow;

import aj.org.objectweb.asm.commons.Remapper;
import com.Non_academicWebsite.Entity.ApprovalFlow.ApprovalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ApprovalFlowRepo extends JpaRepository<ApprovalFlow, Integer> {
    List<ApprovalFlow> findByFormTypeAndDepartment(String formType, String department);
    List<ApprovalFlow> findByFormTypeAndDepartmentAndFaculty(String formType, String department, String faculty);
    List<ApprovalFlow> findByDepartmentAndFaculty(String department, String faculty);
    boolean existsByFormTypeAndDepartment(String formType, String department);
    void deleteByFormTypeAndDepartment(String formType, String department);
}
