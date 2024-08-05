package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.FullLeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface FullLeaveFormRepo extends JpaRepository<FullLeaveForm, Long> {
    List<FullLeaveForm> findByUserIdStartingWith(String prefix);

}
