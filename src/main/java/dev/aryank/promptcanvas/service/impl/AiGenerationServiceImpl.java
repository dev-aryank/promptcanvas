package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.security.AuthUtil;
import dev.aryank.promptcanvas.service.AiGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;

    @PreAuthorize("@security.canEditProject(#projectId)")
    @Override
    public Flux<String> streamResponse(String message, Long projectId) {
        Long userId = authUtil.getCurrentUserId();



        return null;
    }
}
