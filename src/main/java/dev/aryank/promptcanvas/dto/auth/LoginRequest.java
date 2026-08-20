package dev.aryank.promptcanvas.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email @NotBlank String username,
        @NotBlank @Size(min = 4, max = 20) String password
) {
}
