package dev.aryank.promptcanvas.entity;

import dev.aryank.promptcanvas.enums.MessageRole;
import dev.aryank.promptcanvas.enums.PreviewStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
//@Entity
public class ChatMessage {
    Long id;
    ChatSession chatSession;

    String content;

    MessageRole role;

    String toolCalls;

    Integer tokensUsed;

    Instant createdAt;
}
