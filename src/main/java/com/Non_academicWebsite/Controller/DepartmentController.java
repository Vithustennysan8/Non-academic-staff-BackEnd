package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Department;
import com.Non_academicWebsite.Service.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/auth/user/department/getAll")
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping(value = "/auth/user/department/get")
    public ResponseEntity<List<Department>> getAll(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(departmentService.get(header));
    }

    @PostMapping(value = "/admin/department/add")
    public ResponseEntity<List<Department>> add(@RequestBody FacOrDeptDTO facOrDeptDTO,
                                                @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.add(facOrDeptDTO, header));
    }

    @PutMapping(value = "/admin/department/update/{departmentId}")
    public ResponseEntity<List<Department>> update(@PathVariable("departmentId") Integer departmentId,
                                                @RequestBody FacOrDeptDTO facOrDeptDTO,
                                                @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(departmentService.update(facOrDeptDTO, header, departmentId));
    }

    @DeleteMapping(value = "/admin/department/delete/{departmentId}")
    public ResponseEntity<List<Department>> delete(@PathVariable("departmentId") Integer departmentId,
                                                @RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(departmentService.delete(header, departmentId));
    }
}
