package com.mainapp.controller;

import com.mainapp.model.entity.User;
import com.mainapp.security.UserData;
import com.mainapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserData userData;

    @Mock
    private Model model;

    @InjectMocks
    private ProfileController profileController;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setAge(25);

        when(userData.getId()).thenReturn(userId);

        when(userService.getById(userId)).thenReturn(user);
    }

    @Test
    void testGetProfile() {
        String viewName = profileController.getProfile(userData, model);

        verify(userService, times(1)).getById(userId);
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("profile", viewName);
    }

    @Test
    void testUpdateProfile() {
        String newUsername = "updatedUser";
        String newEmail = "updated@example.com";
        int newAge = 30;

        String viewName = profileController.updateProfile(userData, newUsername, newEmail, newAge);

        verify(userService, times(1)).getById(userId);
        verify(userService, times(1)).save(user);

        assertEquals(newUsername, user.getUsername());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newAge, user.getAge());

        assertEquals("redirect:/profile", viewName);
    }
}
