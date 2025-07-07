/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.Class;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ClassService {
    List<Class> getClasses(Map<String, String> params);
    Class getClassById(int id);
    Class addOrUpdateClass(Class c);
    void deleleClass(int id);
    long countClasses(Map<String, String> params);
}
