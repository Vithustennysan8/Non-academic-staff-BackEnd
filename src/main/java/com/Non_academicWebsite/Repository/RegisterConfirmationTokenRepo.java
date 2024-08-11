package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface RegisterConfirmationTokenRepo extends JpaRepository<RegisterConfirmationToken, Long> {
    Optional<RegisterConfirmationToken> findByToken(String token);
    void deleteByUserId(String userId);
}
