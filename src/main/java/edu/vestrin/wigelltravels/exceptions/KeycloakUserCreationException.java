package edu.vestrin.wigelltravels.exceptions;

public class KeycloakUserCreationException extends ApiRuntimeException {
    public KeycloakUserCreationException(int status) {
        super("Failed to createCustomer user in Keycloak. Status: " + status);
    }
}
