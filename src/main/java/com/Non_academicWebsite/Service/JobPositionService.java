package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
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

    public List<JobPosition> add(FacOrDeptDTO jobPositionDto) throws ResourceExistsException {

        if(jobPositionRepo.existsByJobPositionName(jobPositionDto.getName())){
            throw new ResourceExistsException("Job position already exists");
        }
        if(jobPositionRepo.existsByAlias(jobPositionDto.getAlias())){
            throw new ResourceExistsException("Alias already exists, try different");
        }
        JobPosition newJobPosition = JobPosition.builder()
               .jobPositionName(jobPositionDto.getName())
               .alias(jobPositionDto.getAlias())
               .build();
        jobPositionRepo.save(newJobPosition);
        return get();
    }

    public List<JobPosition> update(FacOrDeptDTO facOrDeptDTO, Integer jobPositionId) throws ResourceNotFoundException {
        JobPosition jobPosition = jobPositionRepo.findById(jobPositionId).orElseThrow(
                () -> new ResourceNotFoundException("Job position not found"));

        jobPosition.setJobPositionName(facOrDeptDTO.getName());
        jobPosition.setAlias(facOrDeptDTO.getAlias());
        jobPositionRepo.save(jobPosition);
        return get();
    }

    public List<JobPosition> delete(Integer jobPositionId) throws ResourceNotFoundException {
        JobPosition jobPosition = jobPositionRepo.findById(jobPositionId).orElseThrow(
                () -> new ResourceNotFoundException("Job position not found"));

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
