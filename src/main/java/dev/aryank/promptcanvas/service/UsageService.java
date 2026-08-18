package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.subscription.PlanLimitsResponse;
import dev.aryank.promptcanvas.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
