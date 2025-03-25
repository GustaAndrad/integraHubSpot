package com.example.integrahubspot.controllers;

import com.example.integrahubspot.models.Contact;
import com.example.integrahubspot.services.HubspotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hubspot")
public class HubSpotController {

    private final HubspotService hubspotService;

    public HubSpotController(HubspotService hubspotService) {
        this.hubspotService = hubspotService;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getAuthorizationUrl() {
        return ResponseEntity.ok(hubspotService.getAuthorizationUrl());
    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<String> processOAuthCallback(@RequestParam("code") String code) {
        return hubspotService.exchangeCodeForToken(code);
    }

    @PostMapping("/create-contact")
    public ResponseEntity<String> createContact(@RequestBody Contact contactData) {
        return hubspotService.createContact(contactData);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody List<Map<String, Object>> webhookData, @RequestHeader("X-HubSpot-Signature") String signature) {

        //Melhoria de validação do webhook
        if (!hubspotService.validateWebhook(signature, webhookData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid webhook signature");
        }

        // Pronto para utilização do webhookData

        return ResponseEntity.ok("Webhook received.");
    }

}
