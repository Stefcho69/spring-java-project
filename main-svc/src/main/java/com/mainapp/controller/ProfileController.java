package com.mainapp.controller;

import com.mainapp.model.entity.User;
import com.mainapp.security.UserData;
import com.mainapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfile(@AuthenticationPrincipal UserData userData, Model model) {
        User user = userService.getById(userData.getId());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserData userData,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam int age) {
        User user = userService.getById(userData.getId());

        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);

        userService.save(user);

        return "redirect:/profile";
    }
}
