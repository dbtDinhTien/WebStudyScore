/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.ForumPost;
import com.myapp.pojo.User;
import com.myapp.services.ForumPostService;
import com.myapp.services.UserService;
import static com.mysql.cj.conf.PropertyKey.logger;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
public class ApiForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    
    @Autowired
    private UserService userService;
    
    
    private static final Logger logger = LoggerFactory.getLogger(ApiForumCommentController.class);
    
      // Lấy danh sách bài viết (có thể pagination)
    @GetMapping("/posts/allPost")
    public List<ForumPost> getAllPosts() {
        return forumPostService.getForumPosts(null);
    }
    
        // Lấy chi tiết 1 bài viết
    @GetMapping("/posts/{id}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable("id") int id) {
        ForumPost post = forumPostService.getForumPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }
    
    
    // Tạo bài viết mới
    @PostMapping("/posts/add")
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User author = userService.getUserByEmail(email);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        

        post.setAuthorId(author);
        post.setCreatedAt(new java.util.Date());
        post.setUpdatedAt(new java.util.Date());
        ForumPost savedPost = forumPostService.addOrUpdateForumPost(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }
    
    
     // Cập nhật bài viết
 @PutMapping("/posts/edit/{id}")
public ResponseEntity<?> updatePost(@PathVariable("id") int id, @RequestBody ForumPost updatedPost) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("AUTHENTICATION: " + auth);

    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    String email = auth.getName();

    ForumPost existingPost = forumPostService.getForumPostById(id);
    if (existingPost == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (existingPost.getAuthorId() == null || !existingPost.getAuthorId().getEmail().equals(email)) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    existingPost.setTitle(updatedPost.getTitle());
    existingPost.setContent(updatedPost.getContent());
    existingPost.setUpdatedAt(new java.util.Date());

    try {
        ForumPost savedPost = forumPostService.addOrUpdateForumPost(existingPost);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Lỗi server khi cập nhật bài viết", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @DeleteMapping("/posts/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") int id) {
    try {
        logger.info("Đang cố gắng xoá bài viết ID: {}", id);
        forumPostService.deleteForumPost(id);
        logger.info("Đã xoá thành công bài viết ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    } catch (Exception e) {
        logger.error("Lỗi khi xoá bài viết ID {}:", id, e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


}
