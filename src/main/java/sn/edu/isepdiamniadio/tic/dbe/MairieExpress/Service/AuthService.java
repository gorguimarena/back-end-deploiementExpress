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
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.UserToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {


    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;


    @Value("${jwt.auth.converter.resource-id}")
    private String clientId ;

    private final Map<String,Object> repons = new HashMap<>();

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private KeycloakService keycloakService;

    private final RestTemplate restTemplate = new RestTemplate();


    private final HttpHeaders headers = new HttpHeaders();



    //methode pour se connecter
    public ResponseEntity<Map<String,Object>> authenticate(String username, String password) {
        String url = String.format("%s/protocol/openid-connect/token", authServerUrl);

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format("grant_type=password&client_id=%s&username=%s&password=%s&client_secret=%s",
                clientId, username, password, clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.getBody());
                    repons.put("access_token", root.get("access_token").asText());
                    repons.put("refresh_token", root.get("refresh_token").asText());
                    repons.put("response","succee");
                    return ResponseEntity.ok().body(repons);
                } catch (Exception e) {
                    repons.put("access_token", null);
                    repons.put("response","errorMapping");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(repons);
                }
            } else {
                repons.put("access_token", null);
                repons.put("response","failedAuthentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(repons);
            }
        }catch (Exception e) {
            repons.put("access_token", null);
            repons.put("response","error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(repons);
        }


    }


   /* //methode pour se deconnecter
    public ResponseEntity<Map<String,Object>> logout(String refresh_token) {
        String url = String.format("%s/protocol/openid-connect/logout", authServerUrl);

        headers.setBearerAuth(refresh_token);

        String body = String.format("client_id=%s&client_secret=%s&refresh_token=%s",
                clientId, clientSecret, refresh_token);

        HttpEntity<String> entity = new HttpEntity<>(body,headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                repons.put("response","Deconnection avec succee");
                return ResponseEntity.ok(repons);
            } else {
                repons.put("response","failed");
                return ResponseEntity.ok().body(repons);

            }
        }catch (Exception e) {
            repons.put("response","failed to logout");
            return ResponseEntity.ok().body(repons);
        }
    }*/
   public ResponseEntity<String> logoutUser(String username) {
       try {
           Keycloak keycloak = KeycloakService.connectKeycloak();
           UsersResource usersResource = keycloak.realm(realm).users();

           List<UserRepresentation> userRepresentation = usersResource.search(username,0,1);

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



