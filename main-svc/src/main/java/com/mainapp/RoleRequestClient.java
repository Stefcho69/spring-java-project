package com.mainapp;

import com.mainapp.dto.RoleRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "role-request-service", url = "http://localhost:8081")
public interface RoleRequestClient {

    @PostMapping("/role-requests")
    RoleRequestDTO createRoleRequest(@RequestBody RoleRequestDTO dto);

    @GetMapping("/role-requests")
    List<RoleRequestDTO> getAllRequests();

    @PutMapping("/role-requests/{id}/approve")
    RoleRequestDTO approveRequest(@PathVariable UUID id);
}