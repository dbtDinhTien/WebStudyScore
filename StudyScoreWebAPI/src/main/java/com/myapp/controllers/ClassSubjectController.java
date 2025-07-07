/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.ClassSubject;
import com.myapp.services.ClassSubjectService;
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
public class ClassSubjectController {
    
    @Autowired
    private ClassSubjectService classSubService;
    @Autowired
    private UserService userService;

    @GetMapping("/classSubjects")
    public String listClassSubjects(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");

        long totalClasses = classSubService.countClassSubjects(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) totalClasses / pageSize);

        model.addAttribute("classSubject", new ClassSubject());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("lecturers", userService.getUsersByRole("ROLE_LECTURER"));

        return "classSubject";
    }

    @PostMapping("/classSubjects/add")
    public String add(@ModelAttribute(value = "classSubject") com.myapp.pojo.ClassSubject cs) {
        this.classSubService.addOrUpdateClassSubject(cs);

        return "classSubject";
    }

    @GetMapping("/classSubjects/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("class", this.classSubService.getClassSubjectById(id));

        return "classSubject";
    }

    @PostMapping("/classSubjects/{id}")
    public String updateClass(@PathVariable(value = "id") int id, @ModelAttribute com.myapp.pojo.ClassSubject cs) {
        cs.setId(id);
        classSubService.addOrUpdateClassSubject(cs);
        return "classSubject";
    }

}
