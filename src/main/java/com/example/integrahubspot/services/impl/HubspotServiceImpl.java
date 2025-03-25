package com.example.integrahubspot.services.impl;

import com.example.integrahubspot.dtos.ContactDTO;
import com.example.integrahubspot.models.Contact;
import com.example.integrahubspot.services.HubspotService;
import com.example.integrahubspot.utils.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class HubspotServiceImpl implements HubspotService {

    @Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.client-secret}")
    private String clientSecret;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    private static final String AUTH_URL = "https://app.hubspot.com/oauth/authorize";
    private static final String TOKEN_URL = "https://api.hubapi.com/oauth/v1/token";
    private static final String CONTACTS_URL = "https://api.hubapi.com/crm/v3/objects/contacts";
    private String accessToken;

    //Rate Limit ajustado de acordo com o plano adquirido do hubspot
    private final RateLimiter rateLimiter = new RateLimiter(100, 10);

    public String getAuthorizationUrl() {
        return AUTH_URL + "?client_id="
                + clientId
                + "&redirect_uri="
                + redirectUri
                + "&scope=oauth%20crm.objects.contacts.write"
                + "&response_type=code";
    }

    public ResponseEntity<String> exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            accessToken = (String) response.getBody().get("access_token");
            return ResponseEntity.ok("Access token received successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve access token.");
        }
    }

    public ResponseEntity<String> createContact(Contact contactData) {
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token is missing.");
        }

        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Try again later.");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        ContactDTO contactDTO = new ContactDTO(contactData);
        HttpEntity<ContactDTO> request = new HttpEntity<>(contactDTO, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(CONTACTS_URL, request, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public boolean validateWebhook(String signature, List<Map<String, Object>> webhookData) {
        // Melhoria validacao da assinatura do webhook
        return true;
    }

}
