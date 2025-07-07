/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.Class;
import com.myapp.services.ClassService;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class ClassController {

    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;

    @GetMapping("/classes")
    public String listClasses(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");

        long totalClasses = classService.countClasses(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) totalClasses / pageSize);

        model.addAttribute("class", new Class());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("lecturers", userService.getUsersByRole("ROLE_LECTURER"));

        return "class";
    }

    @PostMapping("/classes/add")
    public String add(@ModelAttribute(value = "class") Class c) {
        this.classService.addOrUpdateClass(c);

        return "class";
    }

    @GetMapping("/classes/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("class", this.classService.getClassById(id));

        return "class";
    }

    @PostMapping("/classes/{id}")
    public String updateClass(@PathVariable(value = "id") int id, @ModelAttribute Class c) {
        c.setId(id);
        classService.addOrUpdateClass(c);
        return "class";
    }

}
