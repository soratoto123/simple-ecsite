package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.security.CustomUserDetails;
import com.example.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "users/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "users/editProfile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Validated @ModelAttribute User updatedUser, BindingResult bindingResult, 
                                @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/editProfile";
        }
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        user.setName(updatedUser.getName());
        user.setPostalCode(updatedUser.getPostalCode());
        user.setAddress(updatedUser.getAddress());
        // 必要に応じて他のフィールドを設定
        userService.updateUserProfile(user);
        return "redirect:/users/profile";
    }
}
