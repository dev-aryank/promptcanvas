package dev.aryank.promptcanvas.entity;

import dev.aryank.promptcanvas.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    ChatSession chatSession;

    @Column(columnDefinition = "text", nullable = false)
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageRole role;

//    String toolCalls;

    Integer tokensUsed = 0;

    @CreationTimestamp
    Instant createdAt;
}
