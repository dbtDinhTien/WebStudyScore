/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.User;
import com.myapp.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");
        String role= params.get("role");

        long totalScores = userService.countUsers(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) totalScores / pageSize);

        model.addAttribute("user", new User());
        model.addAttribute("users", this.userService.getUsers(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);

        return "user";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            Model model) {
        String role = params.get("role");

        if ("ROLE_LECTURER".equals(role)) {
            userService.addLecturer(params, avatar);
        } else {
            userService.addUser(params, avatar);
        }

        return "user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}")
    public String updateUser(@ModelAttribute User u, @PathVariable(value = "id") int id, @RequestParam("oldImage") String oldImage) {
        u.setId(id);

        User existingUser = userService.getUserById(id);

        if (u.getPassword() == null || u.getPassword().isEmpty()) {
            u.setPassword(existingUser.getPassword());
        }

        if (u.getFile() == null || u.getFile().isEmpty()) {
            u.setAvatarUrl(oldImage);
        }
        userService.updateUser(u);
        return "user";
    }
}
