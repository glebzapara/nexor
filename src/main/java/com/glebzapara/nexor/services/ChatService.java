package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat findById(Integer id) {
        return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public List<Chat> findChatsByUserId(Integer id) {
        return chatRepository.findByUsers_Id(id);
    }
}
