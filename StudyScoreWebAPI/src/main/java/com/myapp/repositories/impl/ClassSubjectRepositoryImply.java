/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.ClassSubject;
import com.myapp.repositories.ClassSubjectRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
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
public class ClassSubjectRepositoryImply implements ClassSubjectRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ClassSubject> getClassSubjects(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<ClassSubject> q = cb.createQuery(ClassSubject.class);
        Root<ClassSubject> root = q.from(ClassSubject.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        // Tìm theo tên môn học
        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                // Tìm trong tên môn học
                predicates.add(cb.like(cb.lower(root.get("subjectId").get("subjectName")), "%" + keyword.toLowerCase() + "%"));
                // Tìm trong tên lớp học
                predicates.add(cb.like(cb.lower(root.get("classId").get("name")), "%" + keyword.toLowerCase() + "%"));
                // Tìm trong họ tên giảng viên
                Expression<String> fullName = cb.concat(
                        cb.concat(cb.lower(root.get("lecturerId").get("lastName")), " "),
                        cb.lower(root.get("lecturerId").get("firstName"))
                );
                predicates.add(cb.like(fullName, "%" + keyword.toLowerCase() + "%"));
            }
            if (!predicates.isEmpty()) {
                q.where(cb.or(predicates.toArray(Predicate[]::new)));
            }
        }

        Query query = s.createQuery(q);

        // Phân trang
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public ClassSubject getClassSubjectById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(ClassSubject.class, id);
    }

    @Override
    public ClassSubject addOrUpdateClassSubject(ClassSubject cs) {
        Session se = this.factory.getObject().getCurrentSession();
        if (cs.getId() == null) {
            se.persist(cs);
        } else {
            se.merge(cs);
        }

        return cs;
    }

    @Override
    public void deleleClassSubject(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        ClassSubject cs = this.getClassSubjectById(id);
        se.remove(cs);
    }

    @Override
    public long countClassSubjects(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ClassSubject> root = cq.from(ClassSubject.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("subjectId").get("subjectName")), "%" + keyword.toLowerCase() + "%"));
                predicates.add(cb.like(cb.lower(root.get("classId").get("name")), "%" + keyword.toLowerCase() + "%"));
                Expression<String> fullName = cb.concat(
                        cb.concat(cb.lower(root.get("lecturerId").get("lastName")), " "),
                        cb.lower(root.get("lecturerId").get("firstName"))
                );
                predicates.add(cb.like(fullName, "%" + keyword.toLowerCase() + "%"));
            }
        }
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        }

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public List<ClassSubject> getClassSubjectsByLecturerId(int lecturerId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<ClassSubject> q = cb.createQuery(ClassSubject.class);
        Root<ClassSubject> root = q.from(ClassSubject.class);
        q.select(root);

        Predicate condition = cb.equal(root.get("lecturerId").get("id"),lecturerId);
        q.where(condition);

        return s.createQuery(q).getResultList();
    }

}
