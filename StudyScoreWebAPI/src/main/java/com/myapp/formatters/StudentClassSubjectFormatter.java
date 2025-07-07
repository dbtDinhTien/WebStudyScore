/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.formatters;

import com.myapp.pojo.StudentClassSubject;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class StudentClassSubjectFormatter implements Formatter<StudentClassSubject>{

    @Override
    public String print(StudentClassSubject scs, Locale locale) {
        return String.valueOf(scs.getId());
    }

    @Override
    public StudentClassSubject parse(String id, Locale locale) throws ParseException {
        StudentClassSubject cs = new StudentClassSubject();
        cs.setId(Integer.valueOf(id));
        
        return cs;
    }
    
}
