package com.glebzapara.nexor.controllers.websocket;

import com.glebzapara.nexor.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "chat.messages")
    public void receive(ChatMessageEvent event) {

        messagingTemplate.convertAndSend(
                "/topic/chat/" + event.getChatId(),
                event.getMessage()
        );
    }
}
