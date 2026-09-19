package com.glebzapara.nexor.controllers.websocket;

import com.glebzapara.nexor.dto.MessageRequest;
import com.glebzapara.nexor.dto.MessageResponse;
import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.models.Message;
import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.repositories.ChatRepository;
import com.glebzapara.nexor.repositories.MessageRepository;
import com.glebzapara.nexor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/chat/{id}")
    public MessageResponse sendMessage(
            @DestinationVariable Integer id,
            MessageRequest request,
            Principal principal) {

        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message();

        message.setText(request.getText());
        message.setChat(chat);
        message.setSender(user);
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);

        return new MessageResponse(
                message.getId(),
                message.getText(),
                user.getId(),
                user.getFirstName(),
                message.getCreatedAt()
        );
    }

    @ResponseBody
    @GetMapping("/api/chats/{id}/messages")
    public List<MessageResponse> getMessages(@PathVariable Integer id) {

        return messageRepository.findByChatIdOrderByCreatedAtAsc(id)
                .stream()
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getText(),
                        message.getSender().getId(),
                        message.getSender().getFirstName(),
                        message.getCreatedAt()
                ))
                .toList();
    }
}