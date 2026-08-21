package dev.aryank.promptcanvas.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import dev.aryank.promptcanvas.dto.subscription.CheckoutRequest;
import dev.aryank.promptcanvas.dto.subscription.CheckoutResponse;
import dev.aryank.promptcanvas.dto.subscription.PortalResponse;
import dev.aryank.promptcanvas.entity.Plan;
import dev.aryank.promptcanvas.entity.User;
import dev.aryank.promptcanvas.error.ResourceNotFoundException;
import dev.aryank.promptcanvas.repository.PlanRepository;
import dev.aryank.promptcanvas.repository.UserRepository;
import dev.aryank.promptcanvas.security.AuthUtil;
import dev.aryank.promptcanvas.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {
    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Value("${client.url}")
    private String frontendUrl;

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {
        Plan plan = planRepository.findById(request.planId()).orElseThrow(
                () -> new ResourceNotFoundException("Plan", request.planId().toString())
        );
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", userId.toString())
        );

        SessionCreateParams.Builder params = SessionCreateParams.builder()  // or just use 'var' if you don't understand SessionCreateParams.Builder
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                        .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                        .build())
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());

        try {

            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId == null || stripeCustomerId.isEmpty()) {
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }

            Session session = Session.create(params.build());

            return new CheckoutResponse(session.getUrl());

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
