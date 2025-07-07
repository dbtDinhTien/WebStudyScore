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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiSubjectController {
    @Autowired
    private SubjectService subService;

    // api/subjects/{id}
    @DeleteMapping("/subjects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.subService.deleleSubject(id);
    }
    
    // api/subjects – Danh sách môn học 
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> listSubjects(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(subService.getSubjects(params), HttpStatus.OK);
    }
    
    // api/subjects/{id} – Chi tiết môn học 
    @GetMapping("/subjects/{id}")
    public ResponseEntity<Subject> retrieve(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(subService.getSubjectById(id), HttpStatus.OK);
    }
}
