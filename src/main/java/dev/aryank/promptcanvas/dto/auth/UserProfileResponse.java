package dev.aryank.promptcanvas.dto.auth;

public record UserProfileResponse(
        Long id,
        String username,
        String name
) {
}
