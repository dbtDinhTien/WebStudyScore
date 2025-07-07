/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.StudentClassSubject;
import com.myapp.repositories.StudentClassSubjectRepository;
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
public class StudentClassSubjectRepositoryImply implements StudentClassSubjectRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<StudentClassSubject> getStudentClassSubjects(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<StudentClassSubject> q = cb.createQuery(StudentClassSubject.class);
        Root<StudentClassSubject> root = q.from(StudentClassSubject.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                keyword = keyword.toLowerCase();
                String pattern = "%" + keyword + "%";

                // Tạo predicate dựa trên path mà không join explicit
                Predicate pSubjectName = cb.like(cb.lower(root.get("classSubjectId").get("subjectId").get("subjectName")), pattern);
                Predicate pClassName = cb.like(cb.lower(root.get("classSubjectId").get("classId").get("name")), pattern);
                Predicate pStudentFirstName = cb.like(cb.lower(root.get("studentId").get("firstName")), pattern);
                Predicate pStudentLastName = cb.like(cb.lower(root.get("studentId").get("lastName")), pattern);
                Predicate pStudentCode = cb.like(cb.lower(root.get("studentId").get("studentCode")), pattern);

                predicates.add(cb.or(pSubjectName, pClassName, pStudentFirstName, pStudentLastName, pStudentCode));
            }

            if (!predicates.isEmpty()) {
                q.where(cb.and(predicates.toArray(new Predicate[0])));
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
    public StudentClassSubject getStudentClassSubjectById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(StudentClassSubject.class, id);
    }

    @Override
    public StudentClassSubject addOrUpdateStudentClassSubject(StudentClassSubject scs) {
        Session se = this.factory.getObject().getCurrentSession();
        if (scs.getId() == null) {
            se.persist(scs);
        } else {
            se.merge(scs);
        }

        return scs;
    }

    @Override
    public void deleleStudentClassSubject(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        StudentClassSubject cs = this.getStudentClassSubjectById(id);
        se.remove(cs);
    }

    @Override
    public long countStudentClassSubjects(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<StudentClassSubject> root = cq.from(StudentClassSubject.class);
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        String keyword = params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.toLowerCase();
            String pattern = "%" + keyword + "%";

            // Tạo predicate dựa trên path mà không join explicit
            Predicate pSubjectName = cb.like(cb.lower(root.get("classSubjectId").get("subjectId").get("subjectName")), pattern);
            Predicate pClassName = cb.like(cb.lower(root.get("classSubjectId").get("classId").get("name")), pattern);
            Predicate pStudentFirstName = cb.like(cb.lower(root.get("studentId").get("firstName")), pattern);
            Predicate pStudentLastName = cb.like(cb.lower(root.get("studentId").get("lastName")), pattern);
            Predicate pStudentCode = cb.like(cb.lower(root.get("studentId").get("studentCode")), pattern);

            predicates.add(cb.or(pSubjectName, pClassName, pStudentFirstName, pStudentLastName, pStudentCode));
        }
        cq.where(predicates.toArray(Predicate[]::new));

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public List<StudentClassSubject> getStudentClassSubjectsByStudentId(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<StudentClassSubject> q = cb.createQuery(StudentClassSubject.class);
        Root<StudentClassSubject> root = q.from(StudentClassSubject.class);
        q.select(root);

        Predicate condition = cb.equal(root.get("studentId").get("id"), studentId);
        q.where(condition);

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<StudentClassSubject> getStuClassSubjectByClassSubjectId(int classSubjectId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<StudentClassSubject> q = cb.createQuery(StudentClassSubject.class);
        Root<StudentClassSubject> root = q.from(StudentClassSubject.class);
        q.select(root);

        Predicate condition = cb.equal(root.get("classSubjectId").get("id"), classSubjectId);
        q.where(condition);

        return s.createQuery(q).getResultList();
    }

    @Override
    public StudentClassSubject getByStuCodeAndClassSubjectId(String studentCode, int classSubjectId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<StudentClassSubject> cq = cb.createQuery(StudentClassSubject.class);
        Root<StudentClassSubject> root = cq.from(StudentClassSubject.class);
        cq.select(root);

        Predicate p1 = cb.equal(root.get("studentId").get("studentCode"), studentCode);
        Predicate p2 = cb.equal(root.get("classSubjectId").get("id"), classSubjectId);
        cq.where(cb.and(p1, p2));

        return s.createQuery(cq).uniqueResult();
    }
}
