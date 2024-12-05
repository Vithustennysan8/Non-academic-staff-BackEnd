package com.Non_academicWebsite.Controller;

import com.Non_academicWebsite.DTO.FacOrDeptDTO;
import com.Non_academicWebsite.Entity.JobPosition;
import com.Non_academicWebsite.Service.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api")
public class JobPositionController {

    @Autowired
    private JobPositionService jobPositionService;

    @GetMapping("/auth/user/jobPosition/get")
    public ResponseEntity<List<JobPosition>> get(){
        return ResponseEntity.ok(jobPositionService.get());
    }

    @PostMapping("/admin/jobPosition/add")
    public ResponseEntity<List<JobPosition>> add(@RequestBody FacOrDeptDTO facOrDeptDTO,
                                                 @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(jobPositionService.add(facOrDeptDTO));
    }

    @PutMapping("/admin/jobPosition/update/{jobPositionId}")
    public ResponseEntity<List<JobPosition>> update(@PathVariable("jobPositionId") Integer jobPositionId,
                                                 @RequestBody FacOrDeptDTO facOrDeptDTO,
                                                 @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(jobPositionService.update(facOrDeptDTO, jobPositionId));
    }

    @DeleteMapping("/admin/jobPosition/delete/{jobPositionId}")
    public ResponseEntity<List<JobPosition>> delete(@PathVariable("jobPositionId") Integer jobPositionId,
                                                 @RequestHeader("Authorization") String header){
        return ResponseEntity.ok(jobPositionService.delete(jobPositionId));
    }
}
