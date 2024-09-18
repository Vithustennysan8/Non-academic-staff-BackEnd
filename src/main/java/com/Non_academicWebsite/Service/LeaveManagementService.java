package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.Entity.LeaveManagement;
import com.Non_academicWebsite.Repository.LeaveManagementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveManagementService {
    @Autowired
    private LeaveManagementRepo leaveManagementRepo;

    public List<LeaveManagement> getAllLeaves() {
        return leaveManagementRepo.findAll();
    }

    public LeaveManagement saveLeave(LeaveManagement leave) {
        return leaveManagementRepo.save(leave);
    }
}
