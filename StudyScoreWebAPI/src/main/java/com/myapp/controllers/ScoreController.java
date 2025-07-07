/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.Score;
import com.myapp.services.ScoreService;
import com.myapp.services.UserService;
import com.myapp.services.StudentClassSubjectService;
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
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentClassSubjectService stuClassSubService;

    @GetMapping("/scores")
    public String listScores(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        String keyword = params.get("keyword");

        long totalScores = scoreService.countScores(params);
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) totalScores/ pageSize);

        model.addAttribute("score", new Score());
        model.addAttribute("scores", this.scoreService.getScores(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("students", this.userService.getUsersByRole("ROLE_STUDENT"));
        model.addAttribute("studentClassSubjects", this.stuClassSubService.getStudentClassSubjects(params));

        return "score";
    }

    @PostMapping("/scores/add")
    public String add(@ModelAttribute(value = "score") Score sc) {
        this.scoreService.addOrUpdateScore(sc);
        return "score";
    }

    @GetMapping("/scores/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("score", this.scoreService.getScoreById(id));
        
        return "score";
    }
    
    @PostMapping("/scores/{id}")
    public String updateScore(@PathVariable(value = "id") int id, @ModelAttribute Score sc) {
        sc.setId(id);
        scoreService.addOrUpdateScore(sc);
        return "score";
    }

}
