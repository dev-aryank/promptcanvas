package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.subscription.PlanResponse;
import dev.aryank.promptcanvas.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
