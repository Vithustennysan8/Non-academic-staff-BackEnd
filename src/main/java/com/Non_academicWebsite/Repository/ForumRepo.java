package com.Non_academicWebsite.Repository;

import com.Non_academicWebsite.Entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ForumRepo extends JpaRepository<Forum, Long> {
    List<Forum> findByUserIdStartingWith(String prefix);
    void deleteByUserId(String id);
}
