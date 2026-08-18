package dev.aryank.promptcanvas.dto.member;

import dev.aryank.promptcanvas.enums.ProjectRole;

public record InviteMemberRequest(
        String email,
        ProjectRole role
) {
}
