package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.models.User;
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

    public Chat createChat(User currentUser, User targetUser) {
        Chat chat = new Chat();

        chat.setName(targetUser.getFirstName() + " " + targetUser.getLastName());
        chat.setType("PRIVATE");

        chat.getUsers().add(currentUser);
        chat.getUsers().add(targetUser);

        return chatRepository.save(chat);
    }
}
