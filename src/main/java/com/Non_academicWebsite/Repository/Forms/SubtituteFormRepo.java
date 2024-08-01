package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.SubtituteForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface SubtituteFormRepo extends JpaRepository<SubtituteForm, Long> {
}
