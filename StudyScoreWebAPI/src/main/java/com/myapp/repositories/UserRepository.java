/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories;

import com.myapp.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface UserRepository {
    List<User> getUsers(Map<String, String> params);
    User updateUser(User user);
    void deleleUser(int id);
    long countUsers(Map<String, String> params);
    User getUserByEmail(String email);
    User addUser(User u);
    boolean authenticate(String email, String password);
    List<User> getUsersByRole(String role);
    User getUserById(int id);
    
}
