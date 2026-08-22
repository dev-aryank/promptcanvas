package dev.aryank.promptcanvas.mapper;

import dev.aryank.promptcanvas.dto.subscription.PlanResponse;
import dev.aryank.promptcanvas.dto.subscription.SubscriptionResponse;
import dev.aryank.promptcanvas.entity.Plan;
import dev.aryank.promptcanvas.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {


    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
