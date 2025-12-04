package com.mainapp.controller;

import com.mainapp.RoleRequestClient;
import com.mainapp.dto.RoleRequestDTO;
import com.mainapp.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserRoleRequestController {

    private final RoleRequestClient roleRequestClient;

    public UserRoleRequestController(RoleRequestClient roleRequestClient) {
        this.roleRequestClient = roleRequestClient;
    }

    @PostMapping("request-admin")
    public String requestAdminRole(@AuthenticationPrincipal UserData userData,
                                   @RequestParam String description) {
        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setUserId(userData.getId());
        dto.setUsername(userData.getUsername());
        dto.setRequestedRole("ADMIN");
        dto.setDescription(description);

        roleRequestClient.createRoleRequest(dto);

        return "redirect:/profile";
    }
}