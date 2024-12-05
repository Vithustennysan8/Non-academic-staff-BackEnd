package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.FacultyRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FacultyService {
    
    @Autowired
    private FacultyRepo facultyRepo;
    @Autowired
    private ExtractUserService extractUserService;

    public List<Faculty> getFaculty() {
        return facultyRepo.findAll();
    }

    public List<Faculty> addFaculty(FacOrDeptDTO faculty, String header) {
        Faculty newFaculty = Faculty.builder()
                .facultyName(faculty.getName())
                .alias(faculty.getAlias())
                .isAvailable(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        facultyRepo.save(newFaculty);
        return facultyRepo.findAll();
    }

    public List<Faculty> updateFaculty(FacOrDeptDTO faculty, String header, Integer facultyId) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty facultyToUpdate = facultyRepo.findById(facultyId).orElseThrow(
                () -> new IllegalArgumentException("Faculty not found"));

        facultyToUpdate.setFacultyName(faculty.getName());
        facultyToUpdate.setAlias(faculty.getAlias());
        facultyToUpdate.setUpdatedAt(new Date());
        facultyRepo.save(facultyToUpdate);
        return facultyRepo.findAll();
    }

    public List<Faculty> deleteFaculty(String header, Integer facultyId) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty facultyToDelete = facultyRepo.findById(facultyId).orElseThrow(
                () -> new IllegalArgumentException("Faculty not found"));

        facultyRepo.deleteById(facultyToDelete.getId());
        return facultyRepo.findAll();
    }
}
