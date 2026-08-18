package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.auth.AuthResponse;
import dev.aryank.promptcanvas.dto.auth.LoginRequest;
import dev.aryank.promptcanvas.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
