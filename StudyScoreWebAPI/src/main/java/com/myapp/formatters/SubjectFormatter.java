/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.formatters;

import com.myapp.pojo.Subject;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class SubjectFormatter implements Formatter<Subject>{

    @Override
    public String print(Subject subject, Locale locale) {
        return String.valueOf(subject.getId());
    }

    @Override
    public Subject parse(String id, Locale locale) throws ParseException {
        Subject su = new Subject();
        su.setId(Integer.valueOf(id));
        
        return su;
    }
    
}
