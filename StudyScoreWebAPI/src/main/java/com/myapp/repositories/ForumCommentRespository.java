/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.repositories;

import com.myapp.pojo.ForumComment;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ForumCommentRespository {
     List<ForumComment> getCommentsByPostId (int postId); 
    ForumComment addOrUpdateForumComment(ForumComment forumComment); 
    void deleteForumComment(int id); 
    ForumComment getCommentById(int id); 
}
