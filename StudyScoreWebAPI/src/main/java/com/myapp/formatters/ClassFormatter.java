/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.formatters;

import org.springframework.format.Formatter;
import com.myapp.pojo.Class;
import java.text.ParseException;
import java.util.Locale;
/**
 *
 * @author ADMIN
 */
public class ClassFormatter implements Formatter<Class>{

    @Override
    public String print(Class c, Locale locale) {
        return String.valueOf(c.getId());
    
    }

    @Override
    public Class parse(String id, Locale locale) throws ParseException {
        Class c = new Class();
        c.setId(Integer.valueOf(id));
        
        return c;
    }
    
}
