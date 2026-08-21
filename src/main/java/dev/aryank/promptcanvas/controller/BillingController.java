package dev.aryank.promptcanvas.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import dev.aryank.promptcanvas.dto.subscription.*;
import dev.aryank.promptcanvas.service.PaymentProcessor;
import dev.aryank.promptcanvas.service.PlanService;
import dev.aryank.promptcanvas.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BillingController {

    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final PaymentProcessor paymentProcessor;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody CheckoutRequest request){
        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(request));
    }

    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
    }

//    @PostMapping("/webhooks/payment")
//    public ResponseEntity<String> handlePaymentWebhooks(@RequestBody String payload,
//                                                        @RequestHeader("Stripe-Signature")  String sigHeader){
//
//        try {
//            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
//
//
//
//        } catch (SignatureVerificationException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
