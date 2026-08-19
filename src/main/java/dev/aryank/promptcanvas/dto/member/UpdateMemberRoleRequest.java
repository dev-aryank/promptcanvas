package dev.aryank.promptcanvas.dto.member;

import dev.aryank.promptcanvas.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role
) {
}
