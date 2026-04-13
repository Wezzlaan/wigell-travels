package edu.vestrin.wigelltravels.exceptions;

import com.groupc.shared.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(Long id) {
        super("Could not find customer with id: '%d'".formatted(id));
    }

    public CustomerNotFoundException(String keycloakId) {
        super("Could not find customer with keycloak ID: '%s'".formatted(keycloakId));
    }
}
