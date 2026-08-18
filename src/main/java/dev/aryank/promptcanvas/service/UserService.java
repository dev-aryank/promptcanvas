package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.auth.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(Long userId);
}
