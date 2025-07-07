/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.ClassSubject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ClassSubjectService {
    List<ClassSubject> getClassSubjects(Map<String, String> params);
    ClassSubject getClassSubjectById(int id);
    ClassSubject addOrUpdateClassSubject(ClassSubject cs);
    void deleleClassSubject(int id);
    long countClassSubjects(Map<String, String> params);
    List<ClassSubject> getClassSubjectsByLecturerId(int lecturerId);
}
