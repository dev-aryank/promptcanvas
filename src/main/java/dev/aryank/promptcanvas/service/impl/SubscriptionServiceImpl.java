package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.subscription.CheckoutRequest;
import dev.aryank.promptcanvas.dto.subscription.CheckoutResponse;
import dev.aryank.promptcanvas.dto.subscription.PortalResponse;
import dev.aryank.promptcanvas.dto.subscription.SubscriptionResponse;
import dev.aryank.promptcanvas.entity.Plan;
import dev.aryank.promptcanvas.entity.Subscription;
import dev.aryank.promptcanvas.entity.User;
import dev.aryank.promptcanvas.enums.SubscriptionStatus;
import dev.aryank.promptcanvas.error.ResourceNotFoundException;
import dev.aryank.promptcanvas.mapper.SubscriptionMapper;
import dev.aryank.promptcanvas.repository.PlanRepository;
import dev.aryank.promptcanvas.repository.SubscriptionRepository;
import dev.aryank.promptcanvas.repository.UserRepository;
import dev.aryank.promptcanvas.security.AuthUtil;
import dev.aryank.promptcanvas.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE,
                SubscriptionStatus.TRIALING
        )).orElse(
                new Subscription()
        );

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        boolean exits = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exits) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String subscriptionId) {

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if (subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        // Stripe does not guarantee the order in which webhook events are delivered.
        // For example, invoice.paid may arrive before checkout.session.completed.
        // Since the local Subscription is created during checkout.session.completed,
        // invoice.paid may try to update a subscription that does not exist in our DB yet.
        // Therefore, webhook handlers must be designed to work independently of event order
        // and should not assume that another Stripe event has already been processed.

        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {

    }


//////   Utility methods

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }

}
