package edu.vestrin.wigelltravels.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakUserService {

    private final static Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);
    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUserKeycloak(String email, String username, String password, String firstName, String lastName) {
        logger.info("createUserKeycloak() - Requesting creation of Keycloak User...");
        var user = new UserRepresentation();
        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));

        var usersResource = keycloak.realm(realm).users();

        logger.debug("Sending new User to Keycloak...");
        try (var response = usersResource.create(user)) {
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user in Keycloak. Status: " + response.getStatus());
            }
        }

        List<UserRepresentation> searchResult = usersResource.searchByUsername(username, true);
        if(searchResult.isEmpty()) {
            throw new RuntimeException("User created but could not be found in Keycloak.");
        }

        String newKeycloakId = searchResult.getFirst().getId();

        assignUserRole(usersResource, newKeycloakId);
        return newKeycloakId;
    }

    public void updateUser(String keycloakId, String firstName, String lastName) {
        logger.info("updateUser() - Requesting updating Keycloak User: First Name = {}, Last Name = {}", firstName, lastName);

        var user = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        keycloak.realm(realm).users().get(keycloakId).update(user);
    }

    public void deleteUser(String keycloakId) {
        logger.debug("deleteUser() - Requesting deletion of Customer in Keycloak...");
        keycloak.realm(realm).users().get(keycloakId).remove();
    }

    private void assignUserRole(UsersResource usersResource, String userId) {
        logger.info("assignUserRole() - Assigning role to Keycloak User with ID: {}", userId);
        RoleRepresentation userRole = keycloak.realm(realm).roles().get("USER").toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(List.of(userRole));
    }


}
