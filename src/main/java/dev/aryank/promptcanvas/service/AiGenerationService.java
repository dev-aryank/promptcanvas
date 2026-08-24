package dev.aryank.promptcanvas.service;

import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<String> streamResponse(String message, Long aLong);
}
