/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.Class;
import com.myapp.repositories.ClassRepository;
import com.myapp.services.ClassService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ClassServiceImpl implements ClassService{

    @Autowired
    public ClassRepository classRepo;
    
    @Override
    public List<Class> getClasses(Map<String, String> params) {
        return this.classRepo.getClasses(params);
    }

    @Override
    public Class getClassById(int id) {
        return this.classRepo.getClassById(id);
    }

    @Override
    public Class addOrUpdateClass(Class c) {
        return this.classRepo.addOrUpdateClass(c);
    }

    @Override
    public void deleleClass(int id) {
        this.classRepo.deleleClass(id);
    }

    @Override
    public long countClasses(Map<String, String> params) {
        return this.classRepo.countClasses(params);
    }
    
}
