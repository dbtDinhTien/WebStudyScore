package com.myapp.controllers;

import com.myapp.services.ClassSubjectService;
import com.myapp.pojo.ClassSubject;
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
public class ApiClassSubjectController {
    @Autowired
    private ClassSubjectService classSubService;

    // api/classSubjects/{id}
    @DeleteMapping("/classSubjects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.classSubService.deleleClassSubject(id);
    }
    
    // api/classSubjects – Danh sách môn học 
    @GetMapping("/classSubjects")
    public ResponseEntity<List<ClassSubject>> listClassSubjects(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(classSubService.getClassSubjects(params), HttpStatus.OK);
    }
    
    // api/classSubjects/{id} – Chi tiết môn học 
    @GetMapping("/classSubjects/{id}")
    public ResponseEntity<ClassSubject> retrieve(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(classSubService.getClassSubjectById(id), HttpStatus.OK);
    }
    
    // api//classSubjects/lecturer/{lecturerId} – Chi tiết 
    @GetMapping("/classSubjects/lecturer/{lecturerId}")
    public ResponseEntity<List<ClassSubject>> listClassSubjectsByLecturerId(@PathVariable(value = "lecturerId") int lecturerId){
        return new ResponseEntity<>(classSubService.getClassSubjectsByLecturerId(lecturerId), HttpStatus.OK);
    }
    
}
