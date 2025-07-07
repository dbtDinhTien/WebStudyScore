/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.formatters;

import com.myapp.pojo.User;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 *
 * @author ADMIN
 */
public class UserFormatter implements Formatter<User>{

    @Override
    public String print(User user, Locale locale) {
        return String.valueOf(user.getId());
    }

    @Override
    public User parse(String id, Locale locale) throws ParseException {
        User u = new User();
        u.setId(Integer.valueOf(id));
        
        return u;
    }
    
}
