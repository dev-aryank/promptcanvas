package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.subscription.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlans();
}
