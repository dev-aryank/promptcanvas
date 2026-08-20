package dev.aryank.promptcanvas.dto.member;

import dev.aryank.promptcanvas.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
