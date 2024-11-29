package com.Non_academicWebsite.Repository.ApprovalFlow;

import com.Non_academicWebsite.Entity.ApprovalFlow.FormApprover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface formApproverRepo extends JpaRepository<FormApprover, Long> {
}
