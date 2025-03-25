package com.example.integrahubspot.dtos;

import com.example.integrahubspot.models.Contact;

import java.util.HashMap;
import java.util.Map;

public class ContactDTO {
    private Map<String, Object> properties = new HashMap<>();

    public ContactDTO(Contact contato) {
        properties.put("email", contato.getEmail());
        properties.put("firstname", contato.getFirstname());
        properties.put("lastname", contato.getLastname());
        properties.put("phone", contato.getPhone());
        properties.put("company", contato.getCompany());
        properties.put("website", contato.getWebsite());
        properties.put("lifecyclestage", contato.getLifecyclestage());
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
