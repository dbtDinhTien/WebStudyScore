/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.ForumPost;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface ForumPostService {
    List<ForumPost> getForumPosts(Map<String,String>params); 
    ForumPost getForumPostById(int id);
    ForumPost addOrUpdateForumPost(ForumPost forumPost);
    void deleteForumPost(int id);
}
