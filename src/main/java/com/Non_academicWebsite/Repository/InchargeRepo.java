package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.Incharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface InchargeRepo extends JpaRepository<Incharge, Integer> {
    Optional<Incharge> findByUserId(String id);
}
