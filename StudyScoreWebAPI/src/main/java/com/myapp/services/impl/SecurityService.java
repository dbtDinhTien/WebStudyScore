/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.ClassSubject;
import com.myapp.pojo.StudentClassSubject;
import com.myapp.pojo.User;
import com.myapp.repositories.StudentClassSubjectRepository;
import com.myapp.repositories.UserRepository;
import com.myapp.services.ClassSubjectService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service("securityService")
public class SecurityService {
    @Autowired
    private ClassSubjectService classSubjectService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentClassSubjectRepository studentClassSubjectRepository;

    public boolean isStuClassSubjectOwner(Integer studentClassSubjectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName();

        StudentClassSubject scs = studentClassSubjectRepository.getStudentClassSubjectById(studentClassSubjectId);
        if (scs == null) {
            return false;
        }

        return scs.getStudentId().getEmail().equals(currentUsername);
    }
    
    public boolean isOwner(Integer userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        String currentUsername = auth.getName();

        // Lấy user hiện tại theo username
        User currentUser = userRepository.getUserByEmail(currentUsername);

        if (currentUser == null) return false;

        // So sánh id của user hiện tại với studentId được truyền vào
        return currentUser.getId().intValue() == userId.intValue();
    }
    
    public boolean isLecturerOfClassSubject(int classSubjectId, String username) {
        // Lấy ra classSubject từ DB
        ClassSubject cs = classSubjectService.getClassSubjectById(classSubjectId);

        if (cs == null || cs.getLecturerId() == null) {
            return false;
        }

        // So sánh username của lecturer phụ trách với username hiện tại
        return cs.getLecturerId().getEmail().equals(username);
    }
}
