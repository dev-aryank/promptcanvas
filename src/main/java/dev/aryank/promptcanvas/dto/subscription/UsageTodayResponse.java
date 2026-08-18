package dev.aryank.promptcanvas.dto.subscription;

public record UsageTodayResponse(
        int tokenUsed,
        int tokensLimit,
        int previewsRunning,
        int previewsLimit
) {
}
