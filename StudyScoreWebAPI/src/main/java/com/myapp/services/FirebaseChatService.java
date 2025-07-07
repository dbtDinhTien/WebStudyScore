/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.services;

import com.myapp.pojo.ChatMessage;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public interface FirebaseChatService {
    void sendMessage(String senderId, String receiverId, ChatMessage message);
    String generateRoomId(String senderId, String receiverId);
    List<ChatMessage> getMessages(String roomId);
    List<String> getConversationUserIds(String userId);
}
