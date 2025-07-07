/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.ForumComment;
import com.myapp.repositories.ForumCommentRespository;
import com.myapp.services.ForumCommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ForumCommentServiceImpl implements ForumCommentService {

    @Autowired
    private ForumCommentRespository forumCommentRepository;

    @Override
    public List<ForumComment> getCommentsByPostId(int postId) {
        return forumCommentRepository.getCommentsByPostId(postId);
    }

    @Override
    public ForumComment addOrUpdateForumComment(ForumComment forumComment) {
        return forumCommentRepository.addOrUpdateForumComment(forumComment);
    }

    @Override
    public void deleteForumComment(int id) {
        forumCommentRepository.deleteForumComment(id);
    }

    @Override
    public ForumComment getCommentById(int id) {
        return forumCommentRepository.getCommentById(id);
    }

}