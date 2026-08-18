package dev.aryank.promptcanvas.dto.member;

import dev.aryank.promptcanvas.enums.ProjectRole;

public record UpdateMemberRoleRequest(
        ProjectRole role
) {
}
