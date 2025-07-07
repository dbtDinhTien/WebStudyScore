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
import java.security.Principal;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author DELL
 */
@Controller
public class ForumCommentController {

    @Autowired
    private ForumCommentService commentService;

    @Autowired
    private ForumPostService postService;
    @Autowired
    private UserService userService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ForumCommentController.class);

    @InitBinder("newComment")
    public void initBinderNewComment(WebDataBinder binder) {
        binder.setDisallowedFields("id", "authorId", "postId", "createdAt");
    }

    @InitBinder("comment")
    public void initBinderComment(WebDataBinder binder) {
        binder.setDisallowedFields("authorId", "postId", "createdAt");
    }

    @PostMapping("/forum/{postId}/comment")
    public String addComment(@PathVariable("postId") int postId,
            @ModelAttribute("newComment") ForumComment comment) {
        // Lấy post theo ID
        ForumPost post = postService.getForumPostById(postId);

        // ✅ Lấy email người dùng hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // ✅ Lấy đối tượng User từ email
        User author = userService.getUserByEmail(email);
        if (author == null) {
            throw new RuntimeException("User not found");
        }

        // ✅ Gán author và post cho bình luận
        comment.setAuthorId(author);
        comment.setPostId(post);
        comment.setCreatedAt(new Date());

        // ✅ Gọi service để lưu bình luận
        commentService.addOrUpdateForumComment(comment);

        return "redirect:/forum/" + postId;
    }

    @GetMapping("/forum/comment/edit/{id}")
    public String editCommentForm(@PathVariable("id") int id, Model model) {
        ForumComment comment = commentService.getCommentById(id);
        if (comment == null) {
            // Nếu không tìm thấy thì xử lý hợp lý
            return "redirect:/forum"; // hoặc return error page
        }
        model.addAttribute("comment", comment);
        model.addAttribute("postId", comment.getPostId().getId());
        return "forum-comment-form";
    }




@PostMapping("/forum/comment/update")
public String updateComment(@ModelAttribute("comment") ForumComment updatedComment,
                            @RequestParam("postId") int postId) {
    logger.info("Bắt đầu updateComment, postId = {}, commentId = {}", postId, updatedComment.getId());
    logger.info("Nội dung mới gửi lên: {}", updatedComment.getContent());

    // Lấy thông tin người dùng đang đăng nhập
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    logger.info("Người dùng đăng nhập hiện tại: {}", email);

    User currentUser = userService.getUserByEmail(email);
    if (currentUser == null) {
        logger.error("Không tìm thấy user với email: {}", email);
        return "redirect:/forum/" + postId + "?error=UserNotFound";
    }
    logger.info("User ID hiện tại: {}", currentUser.getId());

    // Lấy bình luận hiện có từ DB
    ForumComment existingComment = commentService.getCommentById(updatedComment.getId());
    if (existingComment == null) {
        logger.error("Không tìm thấy bình luận với id: {}", updatedComment.getId());
        return "redirect:/forum/" + postId + "?error=CommentNotFound";
    }
    logger.info("Bình luận hiện tại trong DB: id={}, nội dung={}", existingComment.getId(), existingComment.getContent());

    // Kiểm tra tác giả bình luận
    if (!existingComment.getAuthorId().getId().equals(currentUser.getId())) {
        logger.warn("Người dùng {} không phải tác giả của bình luận id: {}", currentUser.getId(), existingComment.getId());
        return "redirect:/forum/" + postId + "?error=Unauthorized";
    }

    // Cập nhật nội dung mới
    existingComment.setContent(updatedComment.getContent());
    logger.info("Nội dung bình luận sau khi cập nhật: {}", existingComment.getContent());

    // Lưu lại
    commentService.addOrUpdateForumComment(existingComment);
    logger.info("Đã gọi service lưu bình luận.");

    return "redirect:/forum/" + postId;
}


}
