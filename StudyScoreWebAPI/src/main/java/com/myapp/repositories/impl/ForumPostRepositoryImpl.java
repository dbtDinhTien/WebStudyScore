/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.ForumPost;
import com.myapp.repositories.ForumPostRepository;
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
 * @author DELL
 */

@Repository
@Transactional
public class ForumPostRepositoryImpl implements ForumPostRepository{
    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumPost> getForumPosts(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ForumPost> query = builder.createQuery(ForumPost.class);
        Root<ForumPost> root = query.from(ForumPost.class);
        query.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String title = params.get("title");

            // Tìm kiếm theo tiêu đề nếu có
            if (title != null && !title.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            query.where(predicates.toArray(Predicate[]::new));
        }

        Query q = session.createQuery(query);

        // Xử lý phân trang
        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;
            q.setMaxResults(PAGE_SIZE);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public ForumPost getForumPostById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(ForumPost.class, id);
    }

   

    @Override
    public void deleteForumPost(int id) {
        Session session = factory.getObject().getCurrentSession();
        ForumPost forumPost = this.getForumPostById(id);
        session.remove(forumPost);
    }

    @Override
    public ForumPost addOrUpdateForumPost(ForumPost forumPost) {
Session session = factory.getObject().getCurrentSession();
        if (forumPost.getId() == null) {
            session.persist(forumPost);
        } else {
            session.merge(forumPost);
        }
        return forumPost;    }

    
    
}
