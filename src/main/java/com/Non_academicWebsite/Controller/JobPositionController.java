package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.CustomException.ResourceExistsException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.FacOrDeptOrJobDTO;
import com.Non_academicWebsite.Entity.JobPosition;
import com.Non_academicWebsite.Service.JobPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class JobPositionController {

    private final JobPositionService jobPositionService;

    @GetMapping("/auth/user/jobPosition/get")
    public ResponseEntity<List<JobPosition>> get(){
        return ResponseEntity.ok(jobPositionService.get());
    }

    @PostMapping("/admin/jobPosition/add")
    public ResponseEntity<List<JobPosition>> add(@RequestBody FacOrDeptOrJobDTO facOrDeptDTO,
                                                 @RequestHeader("Authorization") String header)
            throws ResourceExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPositionService.add(facOrDeptDTO));
    }

    @PutMapping("/admin/jobPosition/update/{jobPositionId}")
    public ResponseEntity<List<JobPosition>> update(@PathVariable("jobPositionId") Integer jobPositionId,
                                                 @RequestBody FacOrDeptOrJobDTO facOrDeptDTO,
                                                 @RequestHeader("Authorization") String header)
            throws ResourceNotFoundException, ResourceExistsException {
        return ResponseEntity.ok(jobPositionService.update(facOrDeptDTO, jobPositionId));
    }

    @DeleteMapping("/admin/jobPosition/delete/{jobPositionId}")
    public ResponseEntity<List<JobPosition>> delete(@PathVariable("jobPositionId") Integer jobPositionId,
                                                 @RequestHeader("Authorization") String header)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(jobPositionService.delete(jobPositionId));
    }
}
