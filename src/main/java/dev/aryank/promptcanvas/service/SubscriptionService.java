package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.subscription.CheckoutRequest;
import dev.aryank.promptcanvas.dto.subscription.CheckoutResponse;
import dev.aryank.promptcanvas.dto.subscription.PortalResponse;
import dev.aryank.promptcanvas.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
