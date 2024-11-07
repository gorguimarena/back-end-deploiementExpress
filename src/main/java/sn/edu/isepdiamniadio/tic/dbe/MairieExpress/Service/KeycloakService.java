package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private static String serverUrl;


    @Value("${keycloak.realm}")
    private String realm;


    @Autowired
    private RestTemplate restTemplate;

    public static Keycloak connectKeycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl+"/auth")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
    }


    public ResponseEntity<UserRepresentation> user(String username) {
        Keycloak keycloak = KeycloakService.connectKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.search(username, 0, 1);

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users.getFirst());
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> creerUser(String email, String password, String nom, String prenom, String username, String role) {

        Keycloak keycloak = KeycloakService.connectKeycloak();

        if (usernameExist(keycloak, username)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Ce nom d'utilisateur existe déjà");
        }
        if (emailExists(keycloak, email)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Ce mail existe déjà");
        }

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(nom);
        user.setLastName(prenom);
        user.setEnabled(true);
        user.setEmail(email);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(Collections.singletonList(credential));

        // Création de l'utilisateur
        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de l'utilisateur");
        }

        // Récupération de l'ID de l'utilisateur
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Récupération du rôle à partir du realm
        RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();

        // Attribution du rôle à l'utilisateur
        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        return ResponseEntity.status(HttpStatus.CREATED).body("User created with role " + role);
    }



    public  boolean usernameExist(Keycloak keycloak, String username) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Rechercher par username
        List<UserRepresentation> usersByUsername = usersResource.search(username, 0, 1);
        return !usersByUsername.isEmpty();

    }
    public boolean emailExists(Keycloak keycloak, String email) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Rechercher par email
        List<UserRepresentation> usersByEmail = usersResource.search(null, null, null, email, 0, 1);
        return !usersByEmail.isEmpty();
    }


}
