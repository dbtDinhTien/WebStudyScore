/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.Score;
import com.myapp.pojo.StudentClassSubject;
import com.myapp.pojo.User;
import com.myapp.repositories.ScoreRepository;
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
public class ScoreRepositoryImply implements ScoreRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Score> getScores(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Score> q = cb.createQuery(Score.class);
        Root<Score> root = q.from(Score.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                String kw = "%" + keyword.toLowerCase() + "%";

                // Tìm theo tên môn học
                predicates.add(cb.like(cb.lower(root.get("classSubjectId").get("subjectId").get("subjectName")), kw));

                // Tìm theo tên lớp học
                predicates.add(cb.like(cb.lower(root.get("classSubjectId").get("classId").get("name")), kw));

                // Tìm theo họ tên sinh viên
                Expression<String> fullName = cb.concat(
                        cb.concat(cb.lower(root.get("studentId").get("lastName")), " "),
                        cb.lower(root.get("studentId").get("firstName"))
                );
                predicates.add(cb.like(fullName, kw));
            }
            // Lọc theo classSubjectId
            String classSubjectIdStr = params.get("classSubjectId");
            if (classSubjectIdStr != null && !classSubjectIdStr.isEmpty()) {

                Long classSubjectId = Long.valueOf(classSubjectIdStr);
                predicates.add(cb.equal(root.get("classSubjectId").get("id"), classSubjectId));

            }
            if (!predicates.isEmpty()) {
                q.where(cb.or(predicates.toArray(Predicate[]::new)));
            }
            String orderBy = params.get("orderBy");
            if ("studentName".equals(orderBy)) {
                // Sắp xếp theo họ tên sinh viên: trước theo lastName, nếu trùng thì theo firstName
                q.orderBy(
                        cb.asc(root.get("studentId").get("lastName")),
                        cb.asc(root.get("studentId").get("firstName"))
                );
            } else if (orderBy != null && !orderBy.isEmpty()) {
                // Các trường khác trong Score entity
                q.orderBy(cb.asc(root.get(orderBy)));
            }
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
    public Score getScoreById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Score.class, id);
    }

    @Override
    public Score addOrUpdateScore(Score sc) {
        Session se = this.factory.getObject().getCurrentSession();
        if (sc.getId() == null) {
            se.persist(sc);
        } else {
            se.merge(sc);
        }
        return sc;
    }

    @Override
    public void deleleScore(int id) {
        Session se = this.factory.getObject().getCurrentSession();
        Score sc = this.getScoreById(id);
        se.remove(sc);
    }

    @Override
    public long countScores(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Score> root = q.from(Score.class);
        q.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                String kw = "%" + keyword.toLowerCase() + "%";

                // Tìm theo tên môn học
                predicates.add(cb.like(cb.lower(root.get("classSubjectId").get("subjectId").get("subjectName")), kw));

                // Tìm theo tên lớp học
                predicates.add(cb.like(cb.lower(root.get("classSubjectId").get("classId").get("name")), kw));

                // Tìm theo họ tên sinh viên
                Expression<String> fullName = cb.concat(
                        cb.concat(cb.lower(root.get("studentId").get("lastName")), " "),
                        cb.lower(root.get("studentId").get("firstName"))
                );
                predicates.add(cb.like(fullName, kw));
            }

            if (!predicates.isEmpty()) {
                q.where(cb.or(predicates.toArray(Predicate[]::new)));
            }
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public List<Score> getScoresByClassSubjectId(int classSubjectId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Score> q = cb.createQuery(Score.class);
        Root<Score> root = q.from(Score.class);
        q.select(root);

        Predicate condition = cb.equal(
                root.get("studentClassSubjectId").get("classSubjectId").get("id"),
                classSubjectId
        );

        q.where(condition);

        return s.createQuery(q).getResultList();
    }

    @Override
    public void saveAll(List<Score> scores) {
        Session session = factory.getObject().getCurrentSession();
        for (Score s : scores) {
            session.merge(s);
        }
    }

    @Override
    public List<Score> getScoresByStudentId(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Score> cq = cb.createQuery(Score.class);
        Root<Score> root = cq.from(Score.class);
        cq.select(root);

        Predicate condition = cb.equal(root.get("studentClassSubjectId").get("studentId").get("id"), studentId);
        cq.where(condition);
        return s.createQuery(cq).getResultList();
    }

    @Override
    public Score getScoreByStuClassSubjectId(int stuClassSubjectId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Score> cq = cb.createQuery(Score.class);
        Root<Score> root = cq.from(Score.class);
        cq.select(root);

        Predicate condition = cb.equal(root.get("studentClassSubjectId").get("id"), stuClassSubjectId);
        cq.where(condition);

        return s.createQuery(cq).uniqueResult();
    }
}
