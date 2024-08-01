package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.ShortLeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface ShortLeaveFormRepo extends JpaRepository<ShortLeaveForm, Long> {

}
