/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.User;
import com.myapp.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);

        return (User) q.getSingleResult();

    }

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        return u;
    }

    @Override
    public boolean authenticate(String email, String password) {
        User u = this.getUserByEmail(email);

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<User> getUsersByRole(String role) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createNamedQuery("User.findByRole", User.class);
        q.setParameter("role", role);

        return q.getResultList();
    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo từ khóa tên (họ hoặc tên)
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate firstNameLike = b.like(b.lower(root.get("firstName")), likeKeyword);
                Predicate lastNameLike = b.like(b.lower(root.get("lastName")), likeKeyword);
                Predicate studentCodeLike = b.like(b.lower(root.get("studentCode")), likeKeyword);
                Predicate lecturerCodeLike = b.like(b.lower(root.get("lecturerCode")), likeKeyword);
                predicates.add(b.or(firstNameLike, lastNameLike, studentCodeLike, lecturerCodeLike));
            }

            // Lọc theo vai trò (role)
            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(q);

        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }
        return query.getResultList();
    }

    @Override
    public User updateUser(User user) {
        Session se = this.factory.getObject().getCurrentSession();
        if (user.getId() != null) {
            se.merge(user);
        }

        return user;
    }

    @Override
    public void deleleUser(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        User user = this.getUserById(id);
        se.remove(user);
    }

    @Override
    public long countUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate firstNameLike = b.like(b.lower(root.get("firstName")), likeKeyword);
                Predicate lastNameLike = b.like(b.lower(root.get("lastName")), likeKeyword);
                predicates.add(b.or(firstNameLike, lastNameLike));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(q).getSingleResult();
    }

}
