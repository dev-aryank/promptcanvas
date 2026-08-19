package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.subscription.PlanLimitsResponse;
import dev.aryank.promptcanvas.dto.subscription.UsageTodayResponse;
import dev.aryank.promptcanvas.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
