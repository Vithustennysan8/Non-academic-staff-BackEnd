package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findTopByIdStartingWithOrderByIdDesc(String prefix);
    List<User> findByIdStartingWith(String prefix);
}
