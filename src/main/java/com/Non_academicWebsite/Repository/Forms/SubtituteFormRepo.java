package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.SubtituteForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface SubtituteFormRepo extends JpaRepository<SubtituteForm, Long> {
    List<SubtituteForm> findByUserIdStartingWith(String prefix);
}
