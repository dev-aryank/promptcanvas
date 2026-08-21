package dev.aryank.promptcanvas.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static dev.aryank.promptcanvas.enums.ProjectPermission.*;


@RequiredArgsConstructor
@Getter
public enum ProjectRole {

    OWNER(EDIT, DELETE, MANAGE_MEMBERS, VIEW, VIEW_MEMBERS),
    EDITOR(Set.of(EDIT, VIEW, DELETE, VIEW_MEMBERS)),
    VIEWER(Set.of(VIEW, VIEW_MEMBERS));

    ProjectRole(ProjectPermission... permissions){
        this.permissions = Set.of(permissions);
    }

    private final Set<ProjectPermission> permissions;


}
