package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {


    private static String serverUrl = "http://localhost:8080";

    private String realm = "myRealm";


    private String clientId = "customer1";


    private String clientSecret = "phlyQlUqDQeTaKtwVYeUpm2ntOavZxWo";

    private String grantType = "client_credentials";

    @Autowired
    private RestTemplate restTemplate;

    public static Keycloak connectKeycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
    }


    public UserRepresentation user(String username, String password) {
        Keycloak keycloak = KeycloakService.connectKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();


        List<UserRepresentation> users = usersResource.search(username, 0, 1);

        if (!users.isEmpty()) {
            return users.getFirst();
        }

        return null;
    }

    public ResponseEntity<?> creerUser(String email, String password,String username){

        // Connexion à Keycloak
        Keycloak keycloak = KeycloakService.connectKeycloak();

        if (usernameExist(keycloak, username) || emailExists(keycloak, email)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Username existe");
        }
        // Obtenir le Realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Créer l'utilisateur
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setEmail(email);

        // Ajouter des informations d'identification
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(Collections.singletonList(credential));

        usersResource.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
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
