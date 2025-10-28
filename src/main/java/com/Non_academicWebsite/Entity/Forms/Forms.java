package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface Forms {
    String getFormType();
    String getStatus();
    User getUser();
    LocalDate getLeaveAt();
    int getLeaveDays();

}
