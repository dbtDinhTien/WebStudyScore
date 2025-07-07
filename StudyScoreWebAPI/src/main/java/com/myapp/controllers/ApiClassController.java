/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.Class;
import com.myapp.services.ClassService;
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
public class ApiClassController {
    @Autowired
    private ClassService classService;

    // api/classes/{id}
    @DeleteMapping("/classes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.classService.deleleClass(id);
    }
    
    // api/classes – Danh sách lớp học 
    @GetMapping("/classes")
    public ResponseEntity<List<Class>> listClasses(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(classService.getClasses(params), HttpStatus.OK);
    }
    
    // api/classes/{id} – Chi tiết lớp học 
    @GetMapping("/classes/{id}")
    public ResponseEntity<Class> retrieve(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(classService.getClassById(id), HttpStatus.OK);
    }
}
