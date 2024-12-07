package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private OfficierRepository officierRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AdminMairieRepository adminMairieRepository;

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MairieRepository mairieRepository;

    @Autowired
    private EmailService emailService;

    public static Keycloak connectKeycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
    }


    public ResponseEntity<?> createUserKeycloak(String email, String password, String nom, String prenom,String role,String username) {
        // Connexion à Keycloak
        Keycloak keycloak = connectKeycloak();

        // Obtenir le Realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Obtenir le Realm
        if (emailExists(usersResource, email)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Ce mail existe déjà");
        }

        // Créer l'utilisateur
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setEmailVerified(true);
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

        if (role.equals("citoyen")){
            Optional<Role> role1 = roleRepository.findById(role);
            if (role1.isPresent()) {
                Citoyen citoyen = new Citoyen();
                citoyen.setEmail(email);
                citoyen.setNom(nom);
                citoyen.setPrenom(prenom);
                citoyen.setRoles(Collections.singletonList(role1.get()));
                citoyenRepository.save(citoyen);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }



    public ResponseEntity<?> createAdmin(String email, String password, String nom, String prenom,String role,Integer idMairie, String username) {

        ResponseEntity<?> response = createUserKeycloak(email, password, nom, prenom, role,username);


        if (response.getStatusCode() != HttpStatus.CREATED) {
            if (response.getStatusCode() == HttpStatus.ALREADY_REPORTED){
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response.getBody());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getBody());
        }

         createAdmin(role,email,idMairie,password,nom,prenom,username);

        return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
    }

    public ResponseEntity<?> createAdmin(String role,String email, Integer idMaire,String passsword, String nom, String prenom,String username){

        Optional<Mairie> mairie = mairieRepository.findById(idMaire);
        if (mairie.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Role> role2 = roleRepository.findById(role);

        if (role2.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        switch (role2.get().getCode()){
            case "officier":{
                Officier officier = new Officier();
                officier.setMairie(mairie.get());
                officier.setEmail(email);
                officier.setNom(nom);
                officier.setPrenom(prenom);
                officier.setRoles(Collections.singletonList(role2.get()));
                officierRepository.save(officier);
            }
            break;
            case "agent":{
                Agent agent = new Agent();
                agent.setEmail(email);
                agent.setMairie(mairie.get());
                agent.setNom(nom);
                agent.setPrenom(prenom);
                agent.setRoles(Collections.singletonList(role2.get()));
                agentRepository.save(agent);
            }
            break;
            case "adminMairie":{
                AdminMairie adminMairie = new AdminMairie();
                adminMairie.setEmail(email);
                adminMairie.setMairie(mairie.get());
                adminMairie.setNom(nom);
                adminMairie.setPrenom(prenom);
                adminMairie.setRoles(Collections.singletonList(role2.get()));
                adminMairieRepository.save(adminMairie);
            }
            break;
            default:{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        return sendMessage(email,passsword,username);
    }

    public ResponseEntity<?> sendMessage(String email, String password,String username) {
        String message = String.format(
                "Bonjour, \n\nVotre compte a été créé avec succès. Veuillez trouver ci-dessous vos informations de connexion :\n\n"
                        + "Votre addresse e-mail : %s\nUsername : %s\nMot de passe : %s\n\n"
                        + "Nous vous recommandons de modifier votre mot de passe lors de votre première connexion.\n\n"
                        + "Cordialement,\nL'équipe de gestion des comptes",
                email, username, password
        );
        emailService.sendEmail(email, "Création de votre compte", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // Méthode pour mettre à jour le mot de passe de l'utilisateur
    public ResponseEntity<String> updatePassword(String username, String newPassword) {
        try {
            Keycloak keycloakInstance = connectKeycloak();
            UsersResource usersResource = keycloakInstance.realm(realm).users();

            // Rechercher l'utilisateur par son nom d'utilisateur
            List<UserRepresentation> userRepresentationList = usersResource.search(username, 0, 1);
            if (userRepresentationList.isEmpty()) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé");
            }

            // Récupérer l'utilisateur et définir le nouveau mot de passe
            String userId = userRepresentationList.get(0).getId();
            UserResource userResource = usersResource.get(userId);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);  // Mettre à "true" si le mot de passe doit être temporaire
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);

            userResource.resetPassword(credential);
            return ResponseEntity.ok("Mot de passe mis à jour avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
        }
    }


    // Méthode pour mettre à jour les informations de l'utilisateur
    public ResponseEntity<String> updateUserInfo(String username, Map<String, String> updatedInfo) {
        try {
            Keycloak keycloakInstance = connectKeycloak();
            UsersResource usersResource = keycloakInstance.realm(realm).users();

            // Rechercher l'utilisateur par son nom d'utilisateur
            List<UserRepresentation> userRepresentationList = usersResource.search(username, 0, 1);
            if (userRepresentationList.isEmpty()) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé");
            }

            // Récupérer l'utilisateur et mettre à jour les informations
            String userId = userRepresentationList.getFirst().getId();
            UserResource userResource = usersResource.get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();

            if (updatedInfo.containsKey("email")) {
                userRepresentation.setEmail(updatedInfo.get("email"));
            }

            // Sauvegarder les modifications
            userResource.update(userRepresentation);
            return ResponseEntity.ok("Informations utilisateur mises à jour avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour des informations utilisateur : " + e.getMessage());
        }
    }


    public  boolean usernameExist(UsersResource usersResource, String username) {
        // Rechercher par username
        List<UserRepresentation> usersByUsername = usersResource.search(username, 0, 1);
        return !usersByUsername.isEmpty();

    }
    public String getUsernameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return " ";
        }
        return email.substring(0, email.indexOf('@'));
    }

    public boolean emailExists(UsersResource usersResource, String email) {
        // Rechercher par email
        List<UserRepresentation> usersByEmail = usersResource.search(null, null, null, email, 0, 1);
        return !usersByEmail.isEmpty();
    }

    public ResponseEntity<?> deactivateUserByEmail(String email) {
        // Rechercher l'utilisateur par email
        UserRepresentation user = getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec l'email : " + email);
        }

        // Désactiver l'utilisateur
        return deactivateUser(user.getId());
    }

    public ResponseEntity<?> deactivateUser(String userId) {
        // Connexion à Keycloak
        Keycloak keycloak = connectKeycloak();

        // Obtenir le Realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Récupérer l'utilisateur par son ID
        UserResource userResource = usersResource.get(userId);

        try {
            UserRepresentation user = userResource.toRepresentation();

            // Désactiver l'utilisateur
            user.setEnabled(false);

            // Appliquer les modifications
            userResource.update(user);

            return ResponseEntity.ok("Utilisateur désactivé avec succès");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la désactivation de l'utilisateur");
        }
    }



    public UserRepresentation getUserByEmail(String email) {
        // Connexion à Keycloak
        Keycloak keycloak = connectKeycloak();

        // Obtenir le Realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Rechercher les utilisateurs par email
        List<UserRepresentation> users = usersResource.search(null, null, null, email, null, null);

        // Vérifier si un utilisateur correspond
        if (users != null && !users.isEmpty()) {
            // Retourner le premier utilisateur correspondant
            return users.getFirst();
        }

        // Retourner null si aucun utilisateur trouvé
        return null;
    }
}
