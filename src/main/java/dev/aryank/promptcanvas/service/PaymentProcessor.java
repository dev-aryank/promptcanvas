package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.subscription.CheckoutRequest;
import dev.aryank.promptcanvas.dto.subscription.CheckoutResponse;
import dev.aryank.promptcanvas.dto.subscription.PortalResponse;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal(Long userId);
}
