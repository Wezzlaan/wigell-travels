package edu.vestrin.wigelltravels.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserCreatedButNotFoundInKeycloakException extends ResponseStatusException {

    public UserCreatedButNotFoundInKeycloakException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "User created but could not be found in Keycloak.");
    }
}
