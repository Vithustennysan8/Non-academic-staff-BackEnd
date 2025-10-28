package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.JobPosition;
import com.Non_academicWebsite.Repository.JobPositionRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobPositionService {

    private final JobPositionRepo jobPositionRepo;


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

    public List<JobPosition> update(FacOrDeptDTO jobPositionDto, Integer jobPositionId) throws ResourceNotFoundException, ResourceExistsException {
        JobPosition jobPosition = jobPositionRepo.findById(jobPositionId).orElseThrow(
                () -> new ResourceNotFoundException("Job position not found"));

        if(!Objects.equals(jobPosition.getJobPositionName(), jobPositionDto.getName()) &&
                jobPositionRepo.existsByJobPositionName(jobPositionDto.getName())){
            throw new ResourceExistsException("Job position already exists");
        }
        if(!Objects.equals(jobPosition.getAlias(), jobPositionDto.getAlias()) &&
                jobPositionRepo.existsByAlias(jobPositionDto.getAlias())){
            throw new ResourceExistsException("Alias already exists, try different");
        }

        jobPosition.setJobPositionName(jobPositionDto.getName());
        jobPosition.setAlias(jobPositionDto.getAlias());
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
