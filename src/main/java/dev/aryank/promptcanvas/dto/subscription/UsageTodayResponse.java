package dev.aryank.promptcanvas.dto.subscription;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokensLimit,
        Integer previewsRunning,
        Integer previewsLimit
) {
}
