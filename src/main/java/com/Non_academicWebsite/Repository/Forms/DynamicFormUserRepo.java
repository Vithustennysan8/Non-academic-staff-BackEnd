package com.Non_academicWebsite.Repository.Forms;


import com.Non_academicWebsite.Entity.Forms.DynamicFormUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DynamicFormUserRepo extends JpaRepository<DynamicFormUser, Long> {
    List<DynamicFormUser> findByUserId(String id);
    List<DynamicFormUser> findByUserIdStartingWith(String prefix);
}
