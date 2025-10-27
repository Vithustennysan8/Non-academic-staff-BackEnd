package com.Non_academicWebsite.Controller.Forms;

import com.Non_academicWebsite.CustomException.FormUnderProcessException;
import com.Non_academicWebsite.CustomException.ResourceNotFoundException;
import com.Non_academicWebsite.DTO.Forms.TransferFormDTO;
import com.Non_academicWebsite.Entity.Forms.TransferForm;
import com.Non_academicWebsite.Service.Forms.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/user/transferForm")
public class TransferFormController {
    @Autowired
    private TransferFormService transferFormService;
    @PostMapping(value = "/add")
    public ResponseEntity<TransferForm> addForm(@RequestHeader("Authorization") String header,
                                                    @ModelAttribute TransferFormDTO transferFormDTO,
                                                    @RequestParam(value = "files", required = false) MultipartFile file)
            throws IOException, ResourceNotFoundException {
        return ResponseEntity.ok(transferFormService.add(header, transferFormDTO, file));

    }

    @GetMapping(value = "/getByDepartment")
    public ResponseEntity<List<TransferForm>> getForms(@RequestHeader("Authorization") String header) throws ResourceNotFoundException {
        return ResponseEntity.ok(transferFormService.getForms(header));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<TransferForm>> getFormsOFUser(@RequestHeader("Authorization") String header){
        return ResponseEntity.ok(transferFormService.getFormsOfUser(header));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteByUser(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String header) throws FormUnderProcessException {
        return ResponseEntity.ok(transferFormService.deleteByUser(id, header));
    }
}
