package dev.aryank.promptcanvas.entity;

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
public class ChatSession {

    Project project;
    User user;
    String title;
    Instant createsAt;
    Instant updatedAt;
    Instant deleteAt;

}
