/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.pojo.ChatMessage;
import com.myapp.services.FirebaseChatService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class FirebaseChatServiceImpl implements FirebaseChatService {

    @Override
    public void sendMessage(String senderId, String receiverId, ChatMessage message) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("chatRooms");

        // Đảm bảo roomId đồng nhất theo thứ tự nhỏ -> lớn
        String roomId = senderId.compareTo(receiverId) < 0
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

        ref.child(roomId)
                .child("messages")
                .push()
                .setValueAsync(message);
    }

    @Override
    public String generateRoomId(String senderId, String receiverId) {
        return senderId.compareTo(receiverId) < 0
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;
    }

    @Override
    public List<ChatMessage> getMessages(String roomId) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("chatRooms") 
                .child(roomId)
                .child("messages");

        CompletableFuture<List<ChatMessage>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ChatMessage> messages = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ChatMessage msg = child.getValue(ChatMessage.class);
                    messages.add(msg);
                }
                future.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        try {
            return future.get();  
        } catch (Exception ex) {
            Logger.getLogger(FirebaseChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>(); 
        }
    }
    @Override
    public List<String> getConversationUserIds(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("chatRooms");

        CompletableFuture<List<String>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> partnerIds = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String roomId = child.getKey(); // ví dụ: "123_456"
                    if (roomId.contains(userId)) {
                        // Tách 2 userId từ roomId
                        String[] ids = roomId.split("_");
                        // partnerId là id khác userId
                        String partnerId = ids[0].equals(userId) ? ids[1] : ids[0];
                        partnerIds.add(partnerId);
                    }
                }
                future.complete(partnerIds);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | java.util.concurrent.TimeoutException ex) {
            Logger.getLogger(FirebaseChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

}
