package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;

import java.util.Date;

public interface Forms {
    String getFormType();
    String getStatus();
    User getUser();
    Date getLeaveAt();
    int getLeaveDays();

}
