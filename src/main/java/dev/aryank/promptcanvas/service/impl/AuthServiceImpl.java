package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.auth.AuthResponse;
import dev.aryank.promptcanvas.dto.auth.LoginRequest;
import dev.aryank.promptcanvas.dto.auth.SignupRequest;
import dev.aryank.promptcanvas.entity.User;
import dev.aryank.promptcanvas.error.BadRequestException;
import dev.aryank.promptcanvas.mapper.UserMapper;
import dev.aryank.promptcanvas.repository.UserRepository;
import dev.aryank.promptcanvas.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("Username already exists with username:  " + request.username());
        });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        return new AuthResponse("dummy", userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
