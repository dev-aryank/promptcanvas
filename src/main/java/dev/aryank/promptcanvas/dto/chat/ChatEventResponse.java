package dev.aryank.promptcanvas.dto.chat;

import dev.aryank.promptcanvas.entity.ChatMessage;
import dev.aryank.promptcanvas.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
