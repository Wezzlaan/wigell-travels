package edu.vestrin.wigelltravels.exceptions;

public class CustomerNotFoundException extends ApiResourceNotFoundException{
    public CustomerNotFoundException(Long id) {
        super("Could not find customer with id: '%d'".formatted(id));
    }

    public CustomerNotFoundException(String keycloakId) {
        super("Could not find customer with keycloak ID: '%s'".formatted(keycloakId));
    }
}
