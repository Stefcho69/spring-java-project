package com.restapp.service;

import com.restapp.dto.RoleRequestDTO;
import com.restapp.model.entity.RoleRequest;
import com.restapp.repo.RoleRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RoleRequestService {

    private final RoleRequestRepository repository;

    public RoleRequestService(RoleRequestRepository repository) {
        this.repository = repository;
    }

    public RoleRequest createRoleRequest(RoleRequestDTO dto) {
        RoleRequest rr = new RoleRequest();
        rr.setUserId(dto.getUserId());
        rr.setUsername(dto.getUsername());
        rr.setRequestedRole(dto.getRequestedRole());
        rr.setDescription(dto.getDescription());
        rr.setCreatedOn(LocalDate.now());
        rr.setUpdatedOn(LocalDate.now());

        return repository.save(rr);
    }

    public List<RoleRequest> getAllRequests() {
        return repository.findAll();
    }

    public RoleRequest approveRequest(UUID id) {
        RoleRequest rr = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        rr.setApproved(true);
        rr.setUpdatedOn(LocalDate.now());
        return repository.save(rr);
    }
}
