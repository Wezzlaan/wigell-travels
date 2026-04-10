package edu.vestrin.wigelltravels.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KeycloakUserCreationException extends ResponseStatusException {
    public KeycloakUserCreationException(int status) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user in Keycloak. Status: " + status);
    }
}
