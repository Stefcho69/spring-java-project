package com.restapp.controller;

import com.restapp.dto.RoleRequestDTO;
import com.restapp.model.entity.RoleRequest;
import com.restapp.service.RoleRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/role-requests")
public class RoleRequestController {

    private final RoleRequestService roleRequestService;

    public RoleRequestController(RoleRequestService roleRequestService) {
        this.roleRequestService = roleRequestService;
    }

    @PostMapping
    public RoleRequest createRoleRequest(@RequestBody RoleRequestDTO dto) {
        return roleRequestService.createRoleRequest(dto);
    }

    @GetMapping
    public List<RoleRequest> getAllRequests() {
        return roleRequestService.getAllRequests();
    }

    @PutMapping("/{id}/approve")
    public RoleRequest approveRequest(@PathVariable UUID id) {
        return roleRequestService.approveRequest(id);
    }
}