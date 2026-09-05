package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.chat.ChatResponse;
import dev.aryank.promptcanvas.entity.ChatMessage;
import dev.aryank.promptcanvas.entity.ChatSession;
import dev.aryank.promptcanvas.entity.ChatSessionId;
import dev.aryank.promptcanvas.mapper.ChatMapper;
import dev.aryank.promptcanvas.repository.ChatMessageRepository;
import dev.aryank.promptcanvas.repository.ChatSessionRepository;
import dev.aryank.promptcanvas.security.AuthUtil;
import dev.aryank.promptcanvas.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId, userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessage(chatMessageList);
    }
}
