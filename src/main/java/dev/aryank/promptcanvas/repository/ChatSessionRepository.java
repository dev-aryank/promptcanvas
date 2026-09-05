package dev.aryank.promptcanvas.repository;

import dev.aryank.promptcanvas.entity.ChatSession;
import dev.aryank.promptcanvas.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
