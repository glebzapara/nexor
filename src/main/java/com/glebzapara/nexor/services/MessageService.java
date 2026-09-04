package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.Message;
import com.glebzapara.nexor.repositories.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message findById(Integer id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
    }
}
