package com.Non_academicWebsite.Service.ApprovalFlow;

import com.Non_academicWebsite.CustomException.ApprovalFlowExitsException;
import com.Non_academicWebsite.DTO.ApprovalFlow.ApprovalFlowDTO;
import com.Non_academicWebsite.Entity.ApprovalFlow.ApprovalFlow;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.ApprovalFlow.ApprovalFlowRepo;
import com.Non_academicWebsite.Service.ExtractUser.ExtractUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ApprovalFlowService {

    @Autowired
    private ApprovalFlowRepo approvalFlowRepo;
    @Autowired
    private ExtractUserService extractUserService;

    public Object addNewApprovalFlow(ApprovalFlowDTO approvalFlowDTO, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        if(approvalFlowRepo.existsByFormTypeAndDepartment(approvalFlowDTO.getFormType(), user.getDepartment())) {
            throw new ApprovalFlowExitsException("Approval flow already exists!!!");
        }
        approvalFlowDTO.getApprovalStage().forEach(stage -> {
            ApprovalFlow approvalFlow = ApprovalFlow.builder()
                    .formType(approvalFlowDTO.getFormType())
                    .faculty(user.getFaculty())
                    .department(user.getDepartment())
                    .updatedBy(user.getId())
                    .updatedAt(new Date())
                    .roleName(stage.getRoleName())
                    .sequence(stage.getSequence())
                    .build();
            approvalFlowRepo.save(approvalFlow);
        });

        return getAllApprovalFlowsByDepartment(header);
    }

    @Transactional
    public Object updateApprovalFlow(ApprovalFlowDTO approvalFlowDTO, String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        approvalFlowRepo.deleteByFormTypeAndDepartment(approvalFlowDTO.getFormType(), user.getDepartment());
        addNewApprovalFlow(approvalFlowDTO,header);

         return getAllApprovalFlowsByDepartment(header);
    }

    public Object getAllApprovalFlowsByDepartment(String header) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);
        List<ApprovalFlow> flows = approvalFlowRepo.findByDepartmentAndFaculty(user.getDepartment(), user.getFaculty());

        if (flows.isEmpty()) {
            throw new ApprovalFlowExitsException("No approval flow found for the given department and faculty!!!");
        }

        Map<String, Map<String,Object>> distinctFlow = new HashMap<>();
        for(ApprovalFlow flow : flows) {
            if (!distinctFlow.containsKey(flow.getFormType())) {
                Map<String, Object> distinct = new HashMap<>();
                distinct.put("formType", flow.getFormType());
                distinct.put("faculty", flow.getFaculty());
                distinct.put("department", flow.getDepartment());
                distinct.put("flow", new ArrayList<Map<String, Object>>());
                distinctFlow.put(flow.getFormType(), distinct);
            }

            Map<String, Object> distinct = distinctFlow.get(flow.getFormType());
            List<Map<String, Object>> flowList = (List<Map<String, Object>>) distinct.get("flow");
            flowList.add(Map.of(
                    "roleName", flow.getRoleName(),
                    "sequence", flow.getSequence()
            ));

        }

        for(String form : distinctFlow.keySet()){
            Map<String, Object> approvalFlow = distinctFlow.get(form);
            List<Map<String,Object>> flow = (List<Map<String,Object>>) approvalFlow.get("flow");
            approvalFlow.put("flow",flow.stream().sorted(Comparator.comparingInt(f ->
                    (Integer) f.get("sequence"))).collect(Collectors.toList()));
        }
        return distinctFlow.values();
    }

    @Transactional
    public Object deleteApprovalFlow(String header, ApprovalFlowDTO approvalFlowDTO) {
        User user = extractUserService.extractUserByAuthorizationHeader(header);

        approvalFlowRepo.deleteByFormTypeAndDepartment(approvalFlowDTO.getFormType(), user.getDepartment());
        return getAllApprovalFlowsByDepartment(header);
    }
}
