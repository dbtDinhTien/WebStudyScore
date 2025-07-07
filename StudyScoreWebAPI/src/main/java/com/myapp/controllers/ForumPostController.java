/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.ForumComment;
import com.myapp.pojo.ForumPost;
import com.myapp.pojo.User;
import com.myapp.services.ForumCommentService;
import com.myapp.services.ForumPostService;
import com.myapp.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@Controller
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumCommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping("/forum")
    public String listPosts(Model model) {
        model.addAttribute("posts", forumPostService.getForumPosts(null));
        return "forum";
    }

    @GetMapping("/forum/add")
    public String createPostView(Model model) {
        model.addAttribute("post", new ForumPost());
        return "forum-post-form";
    }

    @PostMapping("/forum/add")
    public String addPost(@ModelAttribute("post") ForumPost post) {
        // Lấy email từ người dùng hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Tìm user theo email
        User author = userService.getUserByEmail(email);
        if (author == null) {
            throw new RuntimeException("User not found");
        }

        // Gán author cho bài viết
        post.setAuthorId(author);
        post.setCreatedAt(new java.util.Date());
        post.setUpdatedAt(new java.util.Date());
        forumPostService.addOrUpdateForumPost(post);
        return "redirect:/forum";
    }

    @GetMapping("/forum/{id}")
    public String viewPostDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("post", forumPostService.getForumPostById(id));
        model.addAttribute("newComment", new ForumComment());
           
        
        return "forum-post-detail";
    }
    @GetMapping("/forum/edit/{id}")
public String editPostForm(@PathVariable("id") int id, Model model) {
    ForumPost post = forumPostService.getForumPostById(id);
    if (post == null) {
        return "redirect:/forum"; // hoặc có thể trả về trang lỗi
    }

    model.addAttribute("post", post);
    return "forum-post-form"; // sử dụng lại form cũ (nếu có thể)
}
@PostMapping("/forum/edit/{id}")
public String editPostSubmit(@PathVariable("id") int id, @ModelAttribute("post") ForumPost post) {
    ForumPost existingPost = forumPostService.getForumPostById(id);
    if (existingPost == null) {
        return "redirect:/forum";
    }

    existingPost.setTitle(post.getTitle());
    existingPost.setContent(post.getContent());
    existingPost.setUpdatedAt(new java.util.Date());

    forumPostService.addOrUpdateForumPost(existingPost);
    return "redirect:/forum";
}

}
