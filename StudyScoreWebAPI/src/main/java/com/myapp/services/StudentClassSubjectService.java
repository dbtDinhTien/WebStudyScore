/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.StudentClassSubject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface StudentClassSubjectService {
    List<StudentClassSubject> getStudentClassSubjects(Map<String, String> params);
    StudentClassSubject getStudentClassSubjectById(int id);
    StudentClassSubject addOrUpdateStudentClassSubject(StudentClassSubject scs);
    void deleleStudentClassSubject(int id);
    long countStudentClassSubjects(Map<String, String> params);
    List<StudentClassSubject> getStudentClassSubjectsByStudentId(int studentId);
    List<StudentClassSubject> getStuClassSubjectByClassSubjectId(int classSubjectId);
    StudentClassSubject getByStuCodeAndClassSubjectId(String studentCode, int classSubjectId);
}
