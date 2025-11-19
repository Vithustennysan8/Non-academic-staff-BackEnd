package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.FacOrDeptOrJobDTO;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.FacultyRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepo facultyRepo;
    private final ExtractUserService extractUserService;

    public List<Faculty> getFaculties() {
        return facultyRepo.findAll();
    }

    public List<Faculty> addFaculty(FacOrDeptOrJobDTO faculty, String header)
            throws ResourceNotFoundException, UnauthorizedAccessException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(facultyRepo.existsByFacultyName(faculty.getName())){
            throw new ResourceExistsException("Faculty already exists");
        }
        if(facultyRepo.existsByAlias(faculty.getAlias())){
            throw new ResourceExistsException("Alias already exists, try new");
        }

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.SUPER_ADMIN){
            Faculty newFaculty = Faculty.builder()
                    .facultyName(faculty.getName())
                    .alias(faculty.getAlias())
                    .isAvailable(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            facultyRepo.save(newFaculty);
            return facultyRepo.findAll();
        }
        throw new UnauthorizedAccessException("No permission to add faculty");
    }

    public List<Faculty> updateFaculty(FacOrDeptOrJobDTO faculty, String header, Integer facultyId) throws ResourceNotFoundException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty facultyToUpdate = facultyRepo.findById(facultyId).orElseThrow(
                () -> new ResourceNotFoundException("Faculty not found"));

        if(!Objects.equals(facultyToUpdate.getAlias(), faculty.getAlias()) &&
                facultyRepo.existsByAlias(faculty.getAlias())){
            throw new ResourceExistsException("Alias already exists, try new");
        }
        if(!Objects.equals(facultyToUpdate.getFacultyName(), faculty.getName()) &&
                facultyRepo.existsByFacultyName(faculty.getName())){
            throw new ResourceExistsException("Faculty already exists");
        }

        facultyToUpdate.setFacultyName(faculty.getName());
        facultyToUpdate.setAlias(faculty.getAlias());
        facultyToUpdate.setUpdatedAt(LocalDateTime.now());
        facultyRepo.save(facultyToUpdate);
        return facultyRepo.findAll();
    }

    public List<Faculty> deleteFaculty(String header, Integer facultyId) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty facultyToDelete = facultyRepo.findById(facultyId).orElseThrow(
                () -> new ResourceNotFoundException("Faculty not found"));

        facultyRepo.deleteById(facultyToDelete.getId());
        return facultyRepo.findAll();
    }

    public Faculty getFac(Integer facId) throws ResourceNotFoundException {
        if (facId == null){
            throw new ResourceNotFoundException("Faculty ID cannot be null");
        }

        Faculty fac = facultyRepo.findById(facId).orElse(null);
        if(fac == null){
            throw new ResourceNotFoundException("Faculty not found");
        }
        return fac;
    }
}
