package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.auth.UserProfileResponse;
import dev.aryank.promptcanvas.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
