package com.Non_academicWebsite.Service;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Department;
import com.Non_academicWebsite.Entity.Faculty;
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

    public List<Department> get(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        return departmentRepo.findAllByFacultyId(faculty.getId());
    }

    public List<Department> add(FacOrDeptDTO facOrDeptDTO, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        if(departmentRepo.existsByFacultyIdAndAlias(faculty.getId(), facOrDeptDTO.getAlias()) &&
        departmentRepo.existsByFacultyIdAndDepartmentName(faculty.getId(), facOrDeptDTO.getName())){
            throw new RuntimeException("Department already exists");
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

    public List<Department> update(FacOrDeptDTO facOrDeptDTO, String header, Integer departmentId) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        Department department = departmentRepo.findById(departmentId).orElse(null);
        if(departmentRepo.existsByFacultyIdAndAlias(faculty.getId(), facOrDeptDTO.getAlias()) &&
                departmentRepo.existsByFacultyIdAndDepartmentName(faculty.getId(), facOrDeptDTO.getName())){
            throw new RuntimeException("Department already exists");
        }

        department.setDepartmentName(facOrDeptDTO.getName());
        department.setAlias(facOrDeptDTO.getAlias());
        department.setUpdatedAt(new Date());
        departmentRepo.save(department);
        return get(header);
    }

    public List<Department> delete(String header, Integer departmentId) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        Faculty faculty = facultyRepo.findByFacultyName(user.getFaculty());
        Department department = departmentRepo.findById(departmentId).orElse(null);
        if (department == null ||!department.getFacultyId().equals(faculty.getId())) {
            throw new RuntimeException("You do not have permission to delete this department");
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
