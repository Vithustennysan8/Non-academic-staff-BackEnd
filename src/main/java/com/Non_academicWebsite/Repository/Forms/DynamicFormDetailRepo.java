package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.DynamicFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DynamicFormDetailRepo extends JpaRepository<DynamicFormDetail, Long> {
    List<DynamicFormDetail> findByDynamicFormUserId(Long id);
}
