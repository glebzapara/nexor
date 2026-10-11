package com.glebzapara.nexor.services;

import com.glebzapara.nexor.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(ChatMessageEvent event) {
        rabbitTemplate.convertAndSend(
                "chat.messages",
                event
        );
    }
}
