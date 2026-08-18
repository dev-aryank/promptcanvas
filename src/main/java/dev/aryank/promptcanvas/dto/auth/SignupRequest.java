package dev.aryank.promptcanvas.dto.auth;

public record SignupRequest(
        String email,
        String name,
        String password
) {
}
