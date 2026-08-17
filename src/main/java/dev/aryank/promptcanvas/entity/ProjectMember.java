package dev.aryank.promptcanvas.entity;

import dev.aryank.promptcanvas.enums.ProjectRole;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
//@Entity
public class ProjectMember{

    ProjectMemberId id;

    Project project;
    User user;

    ProjectRole projectRole;

    Instant  invitedAt;
    Instant acceptedAt;


}
