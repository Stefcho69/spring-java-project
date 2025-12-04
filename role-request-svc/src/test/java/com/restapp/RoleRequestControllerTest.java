package com.restapp;

import com.restapp.dto.RoleRequestDTO;
import com.restapp.model.entity.RoleRequest;
import com.restapp.service.RoleRequestService;
import com.restapp.controller.RoleRequestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleRequestControllerTest {

    @Mock
    private RoleRequestService roleRequestService;

    @InjectMocks
    private RoleRequestController roleRequestController;

    private RoleRequest sampleRequest;
    private RoleRequestDTO sampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDTO = new RoleRequestDTO();
        sampleDTO.setUserId(UUID.randomUUID());
        sampleDTO.setUsername("testUser");
        sampleDTO.setRequestedRole("ADMIN");
        sampleDTO.setDescription("I need admin access");

        sampleRequest = new RoleRequest();
        sampleRequest.setId(UUID.randomUUID());
        sampleRequest.setUserId(sampleDTO.getUserId());
        sampleRequest.setUsername(sampleDTO.getUsername());
        sampleRequest.setRequestedRole(sampleDTO.getRequestedRole());
        sampleRequest.setDescription(sampleDTO.getDescription());
        sampleRequest.setApproved(false);
    }

    @Test
    void testCreateRoleRequest() {
        when(roleRequestService.createRoleRequest(sampleDTO)).thenReturn(sampleRequest);

        RoleRequest result = roleRequestController.createRoleRequest(sampleDTO);

        assertEquals(sampleRequest.getId(), result.getId());
        assertEquals(sampleRequest.getUsername(), result.getUsername());
        verify(roleRequestService, times(1)).createRoleRequest(sampleDTO);
    }

    @Test
    void testGetAllRequests() {
        when(roleRequestService.getAllRequests()).thenReturn(List.of(sampleRequest));

        List<RoleRequest> result = roleRequestController.getAllRequests();

        assertEquals(1, result.size());
        assertEquals(sampleRequest.getId(), result.get(0).getId());
        verify(roleRequestService, times(1)).getAllRequests();
    }

    @Test
    void testApproveRequest() {
        UUID requestId = sampleRequest.getId();
        when(roleRequestService.approveRequest(requestId)).thenReturn(sampleRequest);

        RoleRequest result = roleRequestController.approveRequest(requestId);

        assertEquals(sampleRequest.getId(), result.getId());
        verify(roleRequestService, times(1)).approveRequest(requestId);
    }
}
