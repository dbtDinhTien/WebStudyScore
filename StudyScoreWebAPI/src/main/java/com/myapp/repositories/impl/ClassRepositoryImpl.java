/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;
import com.myapp.pojo.Class;
import com.myapp.repositories.ClassRepository;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class ClassRepositoryImpl implements ClassRepository{
    private static final int PAGE_SIZE = 6;
    
     @Autowired
    private LocalSessionFactoryBean factory;

    // Lấy tất cả lớp với phân trang và tìm kiếm theo tên (nếu có)
    public List<Class> getClasses(Map<String, String> params) {
        Session s =  this.factory.getObject().getCurrentSession();
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Class> q = b.createQuery(Class.class);
            Root<Class> root = q.from(Class.class);
            q.select(root);

            if (params != null) {
                List<Predicate> predicates = new ArrayList<>();

                // Tìm kiếm lớp theo tên (nếu có)
                String name = params.get("keyword");
                if (name != null && !name.isEmpty()) {
                    predicates.add(b.like(root.get("name"), "%" + name + "%"));
                }

                // Áp dụng các điều kiện tìm kiếm
                q.where(predicates.toArray(Predicate[]::new));
            }

            // Phân trang
            Query query = s.createQuery(q);
            if (params != null) {
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                int start = (page - 1)*PAGE_SIZE;
                
                query.setMaxResults(PAGE_SIZE);
                query.setFirstResult(start);
            }

            return query.getResultList();
    }

    // Lấy lớp theo ID
    public Class getClassById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
            return s.get(Class.class, id);
    }

    @Override
    public Class addOrUpdateClass(Class c) {
        Session se = this.factory.getObject().getCurrentSession();
        if (c.getId() == null) {
            se.persist(c);
        } else {
            se.merge(c);
        }

        return c;
    }

    @Override
    public void deleleClass(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        Class s = this.getClassById(id);
        se.remove(s);
    }

    @Override
    public long countClasses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Class> root = q.from(Class.class);
        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        String keyword = params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            predicates.add(b.like(b.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }

        q.where(predicates.toArray(Predicate[]::new));
        return s.createQuery(q).getSingleResult();
    }
}
