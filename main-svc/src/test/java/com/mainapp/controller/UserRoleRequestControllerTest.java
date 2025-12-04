package com.mainapp.controller;

import com.mainapp.RoleRequestClient;
import com.mainapp.dto.RoleRequestDTO;
import com.mainapp.security.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRoleRequestControllerTest {

    @Mock
    private RoleRequestClient roleRequestClient;

    @Mock
    private UserData userData;

    @InjectMocks
    private UserRoleRequestController controller;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();

        when(userData.getId()).thenReturn(userId);
        when(userData.getUsername()).thenReturn("testuser");
    }

    @Test
    void testRequestAdminRole() {
        String description = "I want to become admin";

        String view = controller.requestAdminRole(userData, description);

        ArgumentCaptor<RoleRequestDTO> captor = ArgumentCaptor.forClass(RoleRequestDTO.class);
        verify(roleRequestClient, times(1)).createRoleRequest(captor.capture());

        RoleRequestDTO dto = captor.getValue();
        assertEquals(userId, dto.getUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("ADMIN", dto.getRequestedRole());
        assertEquals(description, dto.getDescription());

        assertEquals("redirect:/profile", view);
    }
}
