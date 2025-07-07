/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.StudentClassSubject;
import com.myapp.services.ClassSubjectService;
import com.myapp.services.StudentClassSubjectService;
import com.myapp.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author ADMIN
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class StudentClassSubjectController {

    @Autowired
    private StudentClassSubjectService stuClassSubService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ClassSubjectService classSubService;

    @GetMapping("/studentClassSubjects")
    public String listStudentClassSubjects(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");

        long total = stuClassSubService.countStudentClassSubjects(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) total / pageSize);
        
        model.addAttribute("studentClassSubject", new StudentClassSubject());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("classSubjects", classSubService.getClassSubjects(params));
        model.addAttribute("students", userService.getUsersByRole("ROLE_STUDENT"));

        // Lấy danh sách để hiển thị
        model.addAttribute("studentClassSubjects", stuClassSubService.getStudentClassSubjects(params));

        return "studentClassSubject";
    }

    @GetMapping("/studentClassSubjects/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("studentClassSubject", stuClassSubService.getStudentClassSubjectById(id));
        return "studentClassSubject";
    }

    @PostMapping("/studentClassSubjects/{id}")
    public String updateStudentClassSubject(@PathVariable(value = "id") int id,
                                            @ModelAttribute StudentClassSubject scs) {
        scs.setId(id);
        stuClassSubService.addOrUpdateStudentClassSubject(scs);
        return "studentClassSubject";
    }

    @PostMapping("/studentClassSubjects/add")
    public String addStudentClassSubject(@ModelAttribute StudentClassSubject scs) {
        stuClassSubService.addOrUpdateStudentClassSubject(scs);
        return "studentClassSubject";
    }

    @GetMapping("/studentClassSubjects/delete/{id}")
    public String deleteStudentClassSubject(@PathVariable("id") int id) {
        stuClassSubService.deleleStudentClassSubject(id);
        return "studentClassSubject";
    }
}

