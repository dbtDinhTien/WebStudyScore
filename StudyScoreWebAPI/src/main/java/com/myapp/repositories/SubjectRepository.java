/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories;

import com.myapp.pojo.Subject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface SubjectRepository {
    List<Subject> getSubjects(Map<String, String> params);
    Subject getSubjectById(int id);
    Subject addOrUpdateSubject(Subject s);
    void deleleSubject(int id);
    long countSubjects(Map<String, String> params);
}
