package com.glebzapara.nexor.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Integer id,
        String text,
        Integer senderId,
        String senderName,
        LocalDateTime createdAt
) {
}
