package dev.aryank.promptcanvas.dto.auth;

public record AuthResponse(
        String token,
        UserProfileResponse userProfileResponse
) {
}
