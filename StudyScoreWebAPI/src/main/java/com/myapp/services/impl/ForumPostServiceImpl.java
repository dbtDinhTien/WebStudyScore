/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.ForumPost;
import com.myapp.repositories.ForumPostRepository;
import com.myapp.services.ForumPostService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ForumPostServiceImpl implements ForumPostService{

    @Autowired
    private ForumPostRepository forumPostRepository; 
    
    
    @Override
    public List<ForumPost> getForumPosts(Map<String, String> params) {
        return forumPostRepository.getForumPosts(params);
    }

    @Override
    public ForumPost getForumPostById(int id) {
        return forumPostRepository.getForumPostById(id);
    }

    @Override
    public ForumPost addOrUpdateForumPost(ForumPost forumPost) {
        return forumPostRepository.addOrUpdateForumPost(forumPost);
    }

    @Override
    public void deleteForumPost(int id) {
        forumPostRepository.deleteForumPost(id);
    }
    
}
