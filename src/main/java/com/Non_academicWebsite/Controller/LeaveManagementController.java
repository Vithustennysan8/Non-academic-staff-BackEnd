package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.Entity.LeaveManagement;
import com.Non_academicWebsite.Service.LeaveManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveManagementController {

        @Autowired
        private LeaveManagementService leaveManagementService;

        @GetMapping
        public List<LeaveManagement> getAllLeaves() {
            return leaveManagementService.getAllLeaves();
        }

        @PostMapping
        public LeaveManagement createLeave(@RequestBody LeaveManagement leave) {
            return leaveManagementService.saveLeave(leave);
        }

}
