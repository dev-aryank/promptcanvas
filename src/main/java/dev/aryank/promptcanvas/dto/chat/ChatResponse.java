package dev.aryank.promptcanvas.dto.chat;

import dev.aryank.promptcanvas.entity.ChatEvent;
import dev.aryank.promptcanvas.entity.ChatSession;
import dev.aryank.promptcanvas.enums.MessageRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        ChatSession chatSession,
        String content,
        MessageRole role,
        List<ChatEvent>events,
        Integer tokensUsed,
        Instant createdAt
) {
}
