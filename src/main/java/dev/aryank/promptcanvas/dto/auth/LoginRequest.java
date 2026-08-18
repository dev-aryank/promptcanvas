package dev.aryank.promptcanvas.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
