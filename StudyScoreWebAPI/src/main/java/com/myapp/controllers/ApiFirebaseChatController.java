/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.ChatMessage;
import com.myapp.pojo.User;
import com.myapp.services.FirebaseChatService;
import com.myapp.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiFirebaseChatController {

    @Autowired
    private FirebaseChatService chatService;
    @Autowired
    private UserService userService;

    @GetMapping("chat/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(
            @RequestParam("senderId") String senderId,
            @RequestParam("receiverId") String receiverId) {
        try {
            String roomId = chatService.generateRoomId(senderId, receiverId);
            List<ChatMessage> messages = chatService.getMessages(roomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("chat/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam("senderId") String senderId,
            @RequestParam("receiverId") String receiverId,
            @RequestBody ChatMessage message) {
        try {
            message.setSenderId(senderId);
            message.setTimestamp(System.currentTimeMillis());
            chatService.sendMessage(senderId, receiverId, message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/chat/conversations")
    public ResponseEntity<List<User>> getConversationUsers(@RequestParam("userId") String userId) {
        try {
            List<String> partnerIdsStr = chatService.getConversationUserIds(userId);
            List<Integer> partnerIds = new ArrayList<>();

            for (String idStr : partnerIdsStr) {
                try {
                    partnerIds.add(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                 
                }
            }

            List<User> users = new ArrayList<>();
            for (Integer id : partnerIds) {
                User u = userService.getUserById(id);
                if (u != null) {
                    users.add(u);
                }
            }

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
