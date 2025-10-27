package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface JobPositionRepo extends JpaRepository<JobPosition, Integer> {
    Optional<JobPosition> findByJobPositionName(String position);
    boolean existsByJobPositionName(String name);
    boolean existsByAlias(String alias);
}
