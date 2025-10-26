package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface NewsRepo extends JpaRepository<News, Integer> {
    @Query("SELECT n FROM News n WHERE n.user.id LIKE CONCAT(:prefix, '%') ORDER BY n.updatedAt DESC")
    List<News> findNews(@Param("prefix") String prefix);
}
