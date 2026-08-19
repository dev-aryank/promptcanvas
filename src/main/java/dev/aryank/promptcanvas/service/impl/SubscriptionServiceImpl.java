package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.subscription.CheckoutRequest;
import dev.aryank.promptcanvas.dto.subscription.CheckoutResponse;
import dev.aryank.promptcanvas.dto.subscription.PortalResponse;
import dev.aryank.promptcanvas.dto.subscription.SubscriptionResponse;
import dev.aryank.promptcanvas.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
