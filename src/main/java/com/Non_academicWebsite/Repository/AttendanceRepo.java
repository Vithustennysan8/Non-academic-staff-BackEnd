package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.Attendance;
import com.Non_academicWebsite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByUserId(User user);
    void deleteByUserId(String userId);
    Optional<Attendance> findByYearAndMonth(String year, String month);
    Optional<Attendance> findByYear(String year);

}
