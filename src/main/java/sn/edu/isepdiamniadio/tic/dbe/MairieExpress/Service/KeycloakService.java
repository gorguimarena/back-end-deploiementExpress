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

@Service
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;


    @Autowired
    private RestTemplate restTemplate;

    public static Keycloak connectKeycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
    }


    public ResponseEntity<?> user(String username) {
        Keycloak keycloak = KeycloakService.connectKeycloak();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        try {
            List<UserRepresentation> users = usersResource.search(username, 0, 1);
            System.out.println("Résultat de la recherche utilisateur : " + users);

            if (!users.isEmpty()) {
                return ResponseEntity.ok(users.getFirst());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne : " + e.getMessage());
        }


        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> creerUser(String username, String password, String prenom, String nom, String email, String role){


        // Connexion à Keycloak
        Keycloak keycloak = connectKeycloak();


        // Obtenir le Realm
        RealmResource realmResource = keycloak.realm("MairieExpress");
        UsersResource usersResource = realmResource.users();

        if (usernameExist(keycloak, username)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Ce nom d'utilisateur existe déjà");
        }
        // Obtenir le Realm
        if (emailExists(keycloak, email)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Ce mail existe déjà");
        }


        // Créer l'utilisateur
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setFirstName(nom);
        user.setLastName(prenom);
        user.setEnabled(true);
        user.setEmail(email);

        // Ajouter des informations d'identification
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(Collections.singletonList(credential));

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

        return ResponseEntity.status(HttpStatus.CREATED).body("User created with role ");

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
