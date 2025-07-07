/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
public interface UserService extends UserDetailsService{
    List<User> getUsers(Map<String, String> params);
    User updateUser(User u);
    void deleleUser(int id);
    long countUsers(Map<String, String> params);
    User getUserByEmail(String email);
    User addUser(Map<String, String> params, MultipartFile avatar);
    boolean authenticate(String email, String password);
    List<User> getUsersByRole(String role);
    User getUserById(int id);
    User addLecturer(Map<String, String> params, MultipartFile avatar);
}
