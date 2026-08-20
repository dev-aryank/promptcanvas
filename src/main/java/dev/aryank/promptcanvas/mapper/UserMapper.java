package dev.aryank.promptcanvas.mapper;

import dev.aryank.promptcanvas.dto.auth.SignupRequest;
import dev.aryank.promptcanvas.dto.auth.UserProfileResponse;
import dev.aryank.promptcanvas.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);

}
