package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.FormOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface FormOptionsRepo extends JpaRepository<FormOption, Long> {
    List<FormOption> findAllByFormFieldId(Long id);
}
