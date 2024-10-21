package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.Role;
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
    Optional<User> findByDepartmentAndFacultyAndRole(String department, String faculty, Role admin);
    List<User> findByIdStartingWithAndVerified(String prefix, boolean verified);
    Optional<User> findByRole(Role role);
}
