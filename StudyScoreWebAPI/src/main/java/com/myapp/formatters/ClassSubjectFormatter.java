/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.formatters;

import com.myapp.pojo.ClassSubject;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class ClassSubjectFormatter implements Formatter<ClassSubject>{

    @Override
    public String print(ClassSubject cs, Locale locale) {
        return String.valueOf(cs.getId());
    }

    @Override
    public ClassSubject parse(String id, Locale locale) throws ParseException {
        ClassSubject cs = new ClassSubject();
        cs.setId(Integer.valueOf(id));
        
        return cs;
    }
    
}
