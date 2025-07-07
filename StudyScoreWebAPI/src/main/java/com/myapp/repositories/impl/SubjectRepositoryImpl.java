/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.Subject;
import com.myapp.repositories.SubjectRepository;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class SubjectRepositoryImpl implements SubjectRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    // Lấy tất cả môn học với phân trang và tìm kiếm theo tên (nếu có)
    @Override
    public List<Subject> getSubjects(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Subject> q = b.createQuery(Subject.class);
        Root<Subject> root = q.from(Subject.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo tên môn học nếu có
            String name = params.get("keyword");
            if (name != null && !name.isEmpty()) {
                predicates.add(b.like(b.lower(root.get("subjectName")), "%" + name.toLowerCase() + "%"));
            }

            // Áp dụng các điều kiện tìm kiếm
            q.where(predicates.toArray(Predicate[]::new));
        }

        // Tạo truy vấn và phân trang
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
    public long countSubjects(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Subject> root = q.from(Subject.class);
        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        String keyword = params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            predicates.add(b.like(b.lower(root.get("subjectName")), "%" + keyword.toLowerCase() + "%"));
        }

        q.where(predicates.toArray(Predicate[]::new));
        return s.createQuery(q).getSingleResult();
    }

    // Lấy môn học theo ID
    @Override
    public Subject getSubjectById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Subject.class, id);
    }

    @Override
    public Subject addOrUpdateSubject(Subject s) {
        Session se = this.factory.getObject().getCurrentSession();
        if (s.getId() == null) {
            se.persist(s);
        } else {
            se.merge(s);
        }

        return s;
    }

    @Override
    public void deleleSubject(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        Subject s = this.getSubjectById(id);
        se.remove(s);
    }

}
