/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.User;
import com.myapp.services.UserService;
import com.myapp.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {
    
    @Autowired
    private UserService userService;

    @PostMapping(path = "/users", 
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestParam Map<String, String> params, @RequestParam(value = "avatar") MultipartFile avatar) {
        return new ResponseEntity<>(this.userService.addUser(params, avatar), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {

        if (this.userService.authenticate(u.getEmail(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getEmail());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userService.getUserByEmail(principal.getName()), HttpStatus.OK);
    }
    
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.userService.deleleUser(id);
    }
    
    @GetMapping("/students/search")
    public ResponseEntity<List<User>> searchStudents(@RequestParam(value = "keyword") String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword != null ? keyword : "");
        params.put("role", "ROLE_STUDENT"); 

        return new ResponseEntity<>(this.userService.getUsers(params), HttpStatus.OK);
    }
    @GetMapping("/lecturers/search")
    public ResponseEntity<List<User>> searchLecturers(@RequestParam(value = "keyword") String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword != null ? keyword : "");
        params.put("role", "ROLE_LECTURER"); 

        return new ResponseEntity<>(this.userService.getUsers(params), HttpStatus.OK);
    }

}
