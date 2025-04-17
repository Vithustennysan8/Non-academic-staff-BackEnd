package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.RegisterConfirmationToken;
import com.Non_academicWebsite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface RegisterConfirmationTokenRepo extends JpaRepository<RegisterConfirmationToken, Long> {
    RegisterConfirmationToken findByToken(String token);

    void deleteByUserId(String userId);

    @Query("SELECT c FROM RegisterConfirmationToken c JOIN c.user u WHERE u.id Like :id% AND u.verified = :status")
    List<RegisterConfirmationToken> findByUserIdPrefixAndVerificationStatus(@Param("id") String prefix, @Param("status") Boolean verify);

    @Query("SELECT c FROM RegisterConfirmationToken c JOIN c.user u WHERE u.role = :role AND u.verified = :status")
    List<RegisterConfirmationToken> findByRoleAndVerificationStatus(@Param("role") Role role, @Param("status") Boolean verify);
}
