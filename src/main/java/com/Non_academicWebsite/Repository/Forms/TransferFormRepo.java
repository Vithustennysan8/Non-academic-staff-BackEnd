package com.Non_academicWebsite.Repository.Forms;

import com.Non_academicWebsite.Entity.Forms.TransferForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface TransferFormRepo extends JpaRepository<TransferForm, Long> {
    List<TransferForm> findByUserIdStartingWith(String prefix);
}
