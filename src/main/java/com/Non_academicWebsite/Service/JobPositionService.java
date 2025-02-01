package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.JobPosition;
import com.Non_academicWebsite.Repository.JobPositionRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPositionService {

    @Autowired
    private JobPositionRepo jobPositionRepo;
    @Autowired
    private ExtractUserService extractUserService;


    public List<JobPosition> get() {
        return jobPositionRepo.findAll();
    }

    public List<JobPosition> add(FacOrDeptDTO facOrDeptDTO) {

        JobPosition newJobPosition = JobPosition.builder()
               .jobPositionName(facOrDeptDTO.getName())
               .alias(facOrDeptDTO.getAlias())
               .build();
        jobPositionRepo.save(newJobPosition);
        return get();
    }

    public List<JobPosition> update(FacOrDeptDTO facOrDeptDTO, Integer jobPositionId) {
        JobPosition jobPosition = jobPositionRepo.findById(jobPositionId).orElse(null);

        if (jobPosition == null) {
            return get();
        }
        jobPosition.setJobPositionName(facOrDeptDTO.getName());
        jobPosition.setAlias(facOrDeptDTO.getAlias());
        jobPositionRepo.save(jobPosition);
        return get();
    }

    public List<JobPosition> delete(Integer jobPositionId) {
        JobPosition jobPosition = jobPositionRepo.findById(jobPositionId).orElse(null);

        if (jobPosition == null) {
            return get();
        }
        jobPositionRepo.delete(jobPosition);
        return get();
    }

    public String getPositionCode(String position) {
        JobPosition jobPosition = jobPositionRepo.findByJobPositionName(position).orElse(null);
        if(jobPosition!= null) {
            return jobPosition.getAlias();
        }
        return null;
    }
}
