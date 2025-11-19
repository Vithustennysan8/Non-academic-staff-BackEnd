package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.FacOrDeptOrJobDTO;
import com.Non_academicWebsite.Entity.Department;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.DepartmentRepo;
import com.Non_academicWebsite.Repository.FacultyRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final ExtractUserService extractUserService;
    private final FacultyRepo facultyRepo;


    public List<Department> getAll() {
        return departmentRepo.findAll();
    }

    public List<Department> get(String header) throws ResourceNotFoundException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if (user.getRole() == Role.SUPER_ADMIN){
            return departmentRepo.findAll();
        }
        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        return departmentRepo.findAllByFacultyId(faculty.getId());
    }

    public List<Department> add(FacOrDeptOrJobDTO facOrDeptDTO, String header)
            throws ResourceNotFoundException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        String facultyName = facOrDeptDTO.getFaculty() == null ? user.getFaculty() : facOrDeptDTO.getFaculty();

        Faculty faculty = facultyRepo.findByFacultyName(facultyName);
        if (faculty == null) {
            throw new ResourceNotFoundException("Faculty not found: " + facultyName);
        }

        if(departmentRepo.existsByFacultyIdAndAlias(faculty.getId(), facOrDeptDTO.getAlias())){
            throw new ResourceExistsException("Alias already exists, try new");
        }
        if(departmentRepo.existsByFacultyIdAndDepartmentName(faculty.getId(), facOrDeptDTO.getName())){
            throw new ResourceExistsException("Department already exists");
        }
        Department newDepartment = Department.builder()
               .departmentName(facOrDeptDTO.getName())
               .alias(facOrDeptDTO.getAlias())
                .facultyId(faculty.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isAvailable(true)
               .build();
        departmentRepo.save(newDepartment);
        return get(header);
    }

    public List<Department> update(FacOrDeptOrJobDTO facOrDeptDTO, String header, Integer departmentId)
            throws ResourceNotFoundException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        String facultyName = facOrDeptDTO.getFaculty() == null ? user.getFaculty() : facOrDeptDTO.getFaculty();

        Faculty faculty = facultyRepo.findByFacultyName(facultyName);
        if (faculty == null) {
            throw new ResourceNotFoundException("Faculty not found: " + facultyName);
        }

        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if(!Objects.equals(department.getAlias(), facOrDeptDTO.getAlias()) &&
                departmentRepo.existsByFacultyIdAndAlias(faculty.getId(), facOrDeptDTO.getAlias())){
            throw new ResourceExistsException("Alias already exists, try new");
        }
        if(!Objects.equals(department.getDepartmentName(), facOrDeptDTO.getName()) &&
                departmentRepo.existsByFacultyIdAndDepartmentName(faculty.getId(), facOrDeptDTO.getName())){
            throw new ResourceExistsException("Department already exists");
        }

        department.setDepartmentName(facOrDeptDTO.getName());
        department.setAlias(facOrDeptDTO.getAlias());
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepo.save(department);
        return get(header);
    }

    public List<Department> delete(String header, Integer departmentId) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        if (faculty == null) {
            throw new ResourceNotFoundException("Faculty not found");
        }

        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (user.getRole() == Role.USER ||
                (user.getRole() != Role.SUPER_ADMIN && !department.getFacultyId().equals(faculty.getId()))) {
            throw new UnauthorizedAccessException("You do not have permission to delete this department");
        }

        departmentRepo.deleteById(departmentId);
        return get(header);
    }

    public String getDepartmentCode(Integer facId, String department) {
        Department departmentEntity = departmentRepo.findByFacultyIdAndDepartmentName(facId, department);
        if (departmentEntity == null) {
            throw new RuntimeException("Department not found");
        }
        return departmentEntity.getAlias();
    }
}
