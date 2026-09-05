package dev.aryank.promptcanvas.repository;

import dev.aryank.promptcanvas.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
