/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.ClassSubject;
import com.myapp.repositories.ClassSubjectRepository;
import com.myapp.services.ClassSubjectService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ClassSubjectServiceImpl implements ClassSubjectService{

    @Autowired
    public ClassSubjectRepository classSubRepo;
    
    @Override
    public List<ClassSubject> getClassSubjects(Map<String, String> params) {
        return this.classSubRepo.getClassSubjects(params);
    }

    @Override
    public ClassSubject getClassSubjectById(int id) {
        return this.classSubRepo.getClassSubjectById(id);
    }

    @Override
    public ClassSubject addOrUpdateClassSubject(ClassSubject cs) {
        return this.classSubRepo.addOrUpdateClassSubject(cs);
    }

    @Override
    public void deleleClassSubject(int id) {
        this.classSubRepo.deleleClassSubject(id);
    }

    @Override
    public long countClassSubjects(Map<String, String> params) {
        return this.classSubRepo.countClassSubjects(params);
    }

    @Override
    public List<ClassSubject> getClassSubjectsByLecturerId(int lecturerId) {
        return this.classSubRepo.getClassSubjectsByLecturerId(lecturerId);
    }
}
