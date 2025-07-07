/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.repositories.impl;

import com.myapp.pojo.ForumComment;
import com.myapp.repositories.ForumCommentRespository;
import java.util.List;
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
public class ForumCommentRepositoryImpl implements ForumCommentRespository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumComment> getCommentsByPostId(int postId) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "FROM ForumComment fc WHERE fc.postId.id = :postId";
        Query<ForumComment> query = session.createQuery(hql, ForumComment.class);
        query.setParameter("postId", postId);
        return query.getResultList();

    }

    @Override
    public ForumComment addOrUpdateForumComment(ForumComment forumComment) {
        Session session = factory.getObject().getCurrentSession();
        if (forumComment.getId() == null) {
            session.persist(forumComment);
        } else {
            session.merge(forumComment);
        }
        return forumComment;
    }

    @Override
    public void deleteForumComment(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumComment forumComment = this.getCommentById(id);
        if (forumComment != null) {
            session.delete(forumComment);
        }
    }

    @Override
    public ForumComment getCommentById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(ForumComment.class, id);
    }
}
