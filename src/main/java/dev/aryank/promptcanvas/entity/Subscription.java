package dev.aryank.promptcanvas.entity;

import dev.aryank.promptcanvas.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
@Getter
@Setter
public class Subscription {
    Long id;

    User user;
    Plan plan;

    SubscriptionStatus status;

    String stripeSubscriptionId;

    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd;

    Instant createdAt;
    Instant updatedAt;
}
