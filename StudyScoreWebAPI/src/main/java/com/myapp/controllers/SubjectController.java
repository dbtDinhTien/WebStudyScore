/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.Subject;
import com.myapp.services.SubjectService;
import java.util.List;
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
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/subjects")
    public String listSubjects(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");

        long totalSubjects = subjectService.countSubjects(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) totalSubjects / pageSize);

        model.addAttribute("subject", new Subject());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);

        return "subject";
    }

    @PostMapping("/subjects/add")
    public String add(@ModelAttribute(value = "subject") Subject s) {
        this.subjectService.addOrUpdateSubject(s);

        return "subject";
    }

    @GetMapping("/subjects/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("subject", this.subjectService.getSubjectById(id));

        return "subject";
    }

    @PostMapping("/subjects/{id}")
    public String updateSubject(@PathVariable(value = "id") int id, @ModelAttribute Subject s, @RequestParam("oldImage") String oldImage) {
        s.setId(id);
        if (s.getFile() == null || s.getFile().isEmpty()) {
            s.setImageUrl(oldImage);
        }
        subjectService.addOrUpdateSubject(s);
        return "subject";
    }

}
