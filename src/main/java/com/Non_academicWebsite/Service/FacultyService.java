package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.FacultyRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FacultyService {
    
    @Autowired
    private FacultyRepo facultyRepo;
    @Autowired
    private ExtractUserService extractUserService;

    public List<Faculty> getFaculties() {
        return facultyRepo.findAll();
    }

    public List<Faculty> addFaculty(FacOrDeptDTO faculty, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        if (user.getRole() == Role.ADMIN
                || user.getRole() == Role.SUPER_ADMIN){
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
        throw new IllegalStateException("No permission to add faculty");
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

    public Faculty getFac(Integer facId) {
        if (facId == null){
            throw new IllegalArgumentException("Faculty ID cannot be null");
        }

        Faculty fac = facultyRepo.findById(facId).orElse(null);
        if(fac == null){
            throw new RuntimeException("Faculty not found");
        }
        return fac;
    }
}
