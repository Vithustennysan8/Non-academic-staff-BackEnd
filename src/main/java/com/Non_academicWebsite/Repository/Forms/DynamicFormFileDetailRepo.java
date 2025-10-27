package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.DynamicFormFileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface DynamicFormFileDetailRepo extends JpaRepository<DynamicFormFileDetail, Long> {
    DynamicFormFileDetail findByDynamicFormUserId(Long id);
    void deleteAllByDynamicFormUserId(Long id);
}
