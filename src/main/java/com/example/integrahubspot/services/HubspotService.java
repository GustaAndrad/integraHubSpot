package com.example.integrahubspot.services;

import com.example.integrahubspot.models.Contact;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface HubspotService {

    String getAuthorizationUrl();

    ResponseEntity<String> exchangeCodeForToken(String code);

    ResponseEntity<String> createContact(Contact contactData);

    boolean validateWebhook(String signature, List<Map<String, Object>> webhookData);
}
