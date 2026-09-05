package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
