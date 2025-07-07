/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.services.ClassService;
import com.myapp.services.SubjectService;
import com.myapp.services.ClassSubjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
@ControllerAdvice
public class IndexController {
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassSubjectService classSubService;
    @Autowired
    private SubjectService subService;
    
    @ModelAttribute
    public void commonResponse(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("subjects", this.subService.getSubjects(params));
        model.addAttribute("classes",this.classService.getClasses(params));
        model.addAttribute("classSubjects",this.classSubService.getClassSubjects(params));
    }
    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params){
        return "index";
    }
}
