package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/auth/user/faculty/getAll")
    public ResponseEntity<List<Faculty>> getFaculty(){
        return ResponseEntity.ok(facultyService.getFaculties());
    }

    @PostMapping("/admin/faculty/add")
    public ResponseEntity<List<Faculty>> addFaculty(@RequestBody FacOrDeptDTO faculty,
                                              @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(facultyService.addFaculty(faculty, header));
    }

    @PutMapping("/admin/faculty/update/{facultyId}")
    public ResponseEntity<List<Faculty>> updateFaculty(@RequestBody FacOrDeptDTO faculty,
                                              @PathVariable("facultyId") Integer facultyId,
                                              @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(facultyService.updateFaculty(faculty, header, facultyId));
    }

    @DeleteMapping("/admin/faculty/delete/{facultyId}")
    public ResponseEntity<List<Faculty>> deleteFaculty(@PathVariable("facultyId") Integer facultyId,
                                              @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(facultyService.deleteFaculty(header, facultyId));
    }
}
