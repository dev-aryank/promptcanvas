package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.service.AiGenerationService;
import reactor.core.publisher.Flux;

public class AiGenerationServiceImpl implements AiGenerationService {

    @Override
    public Flux<String> streamResponse(String message, Long aLong) {
        return null;
    }
}
