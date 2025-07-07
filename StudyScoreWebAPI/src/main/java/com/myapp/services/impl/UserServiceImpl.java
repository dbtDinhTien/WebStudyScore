/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myapp.pojo.User;
import com.myapp.pojo.Class;
import com.myapp.repositories.ClassRepository;
import com.myapp.repositories.UserRepository;
import com.myapp.services.UserService;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClassRepository classRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByEmail(String email) {
        return this.userRepo.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByEmail(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), authorities);
    }

    @Override
    public User addUser(Map<String, String> params, MultipartFile avatar) {
        if (!params.get("email").endsWith("@ou.edu.vn")) {
            throw new IllegalArgumentException("Vui lòng dùng email sinh viên của trường");
        }
        User u = new User();
        u.setFirstName(params.get("first_name"));
        u.setLastName(params.get("last_name"));
        u.setEmail(params.get("email"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setRole("ROLE_STUDENT");
        u.setStudentCode(params.get("student_code"));

        String activeParam = params.get("active");
        boolean active = "on".equals(activeParam);
        u.setActive(active);

        String classIdStr = params.get("class_id");
        if (classIdStr != null && !classIdStr.isEmpty()) {
            Integer classId = Integer.parseInt(classIdStr);
            Class c = this.classRepo.getClassById(classId);
            if (c != null) {
                u.setClassId(c);
            }
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.addUser(u);
    }

    @Override
    public User addLecturer(Map<String, String> params, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(params.get("first_name"));
        u.setLastName(params.get("last_name"));
        u.setEmail(params.get("email"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setRole("ROLE_LECTURER");
        u.setLecturerCode(params.get("lecturer_code"));
        String activeParam = params.get("active");
        boolean active = "on".equals(activeParam);
        u.setActive(active);

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.addUser(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return this.userRepo.getUsersByRole(role);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public void deleleUser(int id) {
        this.userRepo.deleleUser(id);
    }

    @Override
    public long countUsers(Map<String, String> params) {
        return this.userRepo.countUsers(params);
    }

    @Override
    public User updateUser(User u) {
        // Nếu có mật khẩu mới (bạn có thể thêm field `newPassword` tạm thời nếu muốn tách riêng)
        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            u.setPassword(this.passwordEncoder.encode(u.getPassword()));
        }

        // Nếu có file ảnh, upload lên Cloudinary
        if (!u.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(u.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.updateUser(u);
    }
}
