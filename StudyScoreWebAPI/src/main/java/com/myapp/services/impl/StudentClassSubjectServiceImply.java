/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.StudentClassSubject;
import com.myapp.repositories.StudentClassSubjectRepository;
import com.myapp.services.StudentClassSubjectService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class StudentClassSubjectServiceImply implements StudentClassSubjectService {

    @Autowired
    public StudentClassSubjectRepository stuClassSubRepo;

    @Override
    public List<StudentClassSubject> getStudentClassSubjects(Map<String, String> params) {
        return this.stuClassSubRepo.getStudentClassSubjects(params);
    }

    @Override
    public StudentClassSubject getStudentClassSubjectById(int id) {
        return this.stuClassSubRepo.getStudentClassSubjectById(id);
    }

    @Override
    public StudentClassSubject addOrUpdateStudentClassSubject(StudentClassSubject scs) {
        return this.stuClassSubRepo.addOrUpdateStudentClassSubject(scs);
    }

    @Override
    public void deleleStudentClassSubject(int id) {
        this.stuClassSubRepo.deleleStudentClassSubject(id);
    }

    @Override
    public long countStudentClassSubjects(Map<String, String> params) {
        return this.stuClassSubRepo.countStudentClassSubjects(params);
    }

    @Override
    public List<StudentClassSubject> getStudentClassSubjectsByStudentId(int studentId) {
        return this.stuClassSubRepo.getStudentClassSubjectsByStudentId(studentId);
    }

    @Override
    public List<StudentClassSubject> getStuClassSubjectByClassSubjectId(int classSubjectId) {
        return this.stuClassSubRepo.getStuClassSubjectByClassSubjectId(classSubjectId);
    }

    @Override
    public StudentClassSubject getByStuCodeAndClassSubjectId(String studentCode, int classSubjectId) {
        return this.stuClassSubRepo.getByStuCodeAndClassSubjectId(studentCode, classSubjectId);
    }

}
