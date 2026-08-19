package dev.aryank.promptcanvas.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberId {

    Long projectId;
    Long userId;

}
