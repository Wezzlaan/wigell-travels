package edu.vestrin.wigelltravels.exceptions;

public class UserCreatedButNotFoundInKeycloakException extends ApiRuntimeException {

    public UserCreatedButNotFoundInKeycloakException() {
        super("User created but could not be found in Keycloak.");
    }
}
