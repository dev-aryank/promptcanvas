package dev.aryank.promptcanvas.controller;


import dev.aryank.promptcanvas.dto.auth.AuthResponse;
import dev.aryank.promptcanvas.dto.auth.LoginRequest;
import dev.aryank.promptcanvas.dto.auth.SignupRequest;
import dev.aryank.promptcanvas.dto.auth.UserProfileResponse;
import dev.aryank.promptcanvas.service.AuthService;
import dev.aryank.promptcanvas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L; //todo: change it later to securityContext
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
