/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.ForumComment;
import com.myapp.pojo.User;
import com.myapp.services.ForumCommentService;
import com.myapp.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
public class ApiForumCommentController {

    @Autowired
    private ForumCommentService commentService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ApiForumCommentController.class);

    // Lấy danh sách bình luận theo bài viết
    @GetMapping("/comments/post/{postId}")
    public ResponseEntity<List<ForumComment>> getCommentsByPost(@PathVariable("postId") int postId) {
        try {
            List<ForumComment> comments = commentService.getCommentsByPostId(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Lỗi lấy bình luận cho bài viết ID: {}", postId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/comments/add")
    public ResponseEntity<ForumComment> createComment(@RequestBody ForumComment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User author = null;
        try {
            author = userService.getUserByEmail(email);
        } catch (jakarta.persistence.NoResultException ex) {
            // Bắt exception khi không tìm thấy user
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (author == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        comment.setAuthorId(author);
        comment.setCreatedAt(new java.util.Date());
        ForumComment savedComment = commentService.addOrUpdateForumComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/edit/{id}")
    public ResponseEntity<ForumComment> updateComment(@PathVariable("id") int id,
            @RequestBody ForumComment updatedComment) {
        try {
            ForumComment existingComment = commentService.getCommentById(id);
            if (existingComment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Lấy người dùng hiện tại
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userService.getUserByEmail(email);

            // Kiểm tra quyền chỉnh sửa
            if (!existingComment.getAuthorId().getId().equals(currentUser.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // Cập nhật nội dung bình luận
            existingComment.setContent(updatedComment.getContent());

            // Gọi service lưu lại
            ForumComment savedComment = commentService.addOrUpdateForumComment(existingComment);
            return new ResponseEntity<>(savedComment, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật bình luận ID {}: ", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") int id) {
        try {
            logger.info("Đang cố gắng xoá : {}", id);
            commentService.deleteForumComment(id);
            logger.info("Đã xoá thành công: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Đã xảy ra lỗi khi xoá sự kiện với ID {}: ", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
