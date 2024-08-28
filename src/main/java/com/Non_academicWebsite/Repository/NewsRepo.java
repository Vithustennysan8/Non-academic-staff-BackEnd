package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface NewsRepo extends JpaRepository<News, Integer> {
    List<News> findByUserIdStartingWith(String prefix);
}
