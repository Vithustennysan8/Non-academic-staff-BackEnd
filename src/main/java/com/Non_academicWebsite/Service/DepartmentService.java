package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.CustomException.UnauthorizedAccessException;
import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Department;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.Role;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.DepartmentRepo;
import com.Non_academicWebsite.Repository.FacultyRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private ExtractUserService extractUserService;
    @Autowired
    private FacultyRepo facultyRepo;


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

    public List<Department> add(FacOrDeptDTO facOrDeptDTO, String header)
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
                .createdAt(new Date())
                .updatedAt(new Date())
                .isAvailable(true)
               .build();
        departmentRepo.save(newDepartment);
        return get(header);
    }

    public List<Department> update(FacOrDeptDTO facOrDeptDTO, String header, Integer departmentId)
            throws ResourceNotFoundException, ResourceExistsException {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        String facultyName = facOrDeptDTO.getFaculty() == null ? user.getFaculty() : facOrDeptDTO.getFaculty();

        Faculty faculty = facultyRepo.findByFacultyName(facultyName);
        if (faculty == null) {
            throw new ResourceNotFoundException("Faculty not found: " + facultyName);
        }

        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if(departmentRepo.existsByFacultyIdAndAlias(faculty.getId(), facOrDeptDTO.getAlias())){
            throw new ResourceExistsException("Alias already exists, try new");
        }
        if(departmentRepo.existsByFacultyIdAndDepartmentName(faculty.getId(), facOrDeptDTO.getName())){
            throw new ResourceExistsException("Department already exists");
        }

        department.setDepartmentName(facOrDeptDTO.getName());
        department.setAlias(facOrDeptDTO.getAlias());
        department.setUpdatedAt(new Date());
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
