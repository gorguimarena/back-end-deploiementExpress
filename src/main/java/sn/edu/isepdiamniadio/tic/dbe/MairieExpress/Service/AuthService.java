package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Utilisateur;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.UserToken;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.UtilisateurRepository;

import java.util.*;

@Service
public class AuthService {


    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;


    @Value("${jwt.auth.converter.resource-id}")
    private String clientId;

    private final Map<String, Object> repons = new HashMap<>();

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UtilisateurRepository userRepository;

    public ResponseEntity<Map<String, Object>> authenticateUser(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Initialisation de la connexion à Keycloak pour l'utilisateur : {}", email);
            Keycloak keycloak = KeycloakBuilder
                    .builder()
                    .serverUrl("http://localhost:8080")
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(email)
                    .password(password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .build();

            AccessTokenResponse tokenResponse;
            try {
                logger.info("Tentative de récupération du token d'accès pour l'utilisateur : {}", email);
                tokenResponse = keycloak.tokenManager().getAccessToken();
            } catch (Exception e) {
                logger.error("Erreur lors de la récupération du token d'accès pour l'utilisateur {}: {}", email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if (tokenResponse != null) {
                Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    Utilisateur user = userOptional.get();
                    response.put("access_token", tokenResponse.getToken());
                    response.put("refresh_token", tokenResponse.getRefreshToken());
                    response.put("response", "success");
                    response.put("userId", user.getId());
                    response.put("role", user.getRoles().getFirst());
                    logger.info("Authentification réussie pour l'utilisateur : {}", email);
                    return ResponseEntity.ok(response);
                } else {
                    logger.warn("Utilisateur non trouvé : {}", email);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                logger.error("Echec de l'authentification pour l'utilisateur : {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            logger.error("Erreur interne lors de l'authentification de l'utilisateur {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<?> logoutUser(Integer id) {
        try {
            Keycloak keycloak = KeycloakService.connectKeycloak();
            UsersResource usersResource = keycloak.realm(realm).users();

            Optional<Utilisateur> utilisateur = userRepository.findById(id);
            if (utilisateur.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            String email = utilisateur.get().getEmail();

            List<UserRepresentation> userRepresentation = usersResource.search(null, null, null, email, 0, 1);

            if (userRepresentation.isEmpty()) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé");
            }

            String sessionId = userRepresentation.getFirst().getId();
            usersResource.get(sessionId).logout();
            return ResponseEntity.ok("Déconnexion réussie");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la déconnexion");
        }
    }

}



