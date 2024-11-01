package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final String authServerUrl = "http://localhost:8080";


    private final String realm = "myRealm";


    private String clientId = "customer1";

    private final Map<String,Object> repons = new HashMap<>();

    private String clientSecret = "phlyQlUqDQeTaKtwVYeUpm2ntOavZxWo";


    private final RestTemplate restTemplate = new RestTemplate();


    private final HttpHeaders headers = new HttpHeaders();



    //methode pour se connecter
    public ResponseEntity<Map<String,Object>> authenticate(String username, String password) {
        String url = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

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


    //methode pour se deconnecter
    public ResponseEntity<Map<String,Object>> logout(String refresh_token) {
        String url = String.format("%s/realms/%s/protocol/openid-connect/logout", authServerUrl, realm);

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
    }

    // Méthode pour créer un utilisateur avec un mot de passe
    public ResponseEntity<Map<String, Object>> createUser(String username, String email, String password,String tokenAdmin) {
        String url = String.format("%s/admin/realms/%s/users", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("client_id",clientSecret);
        user.put("enabled", true);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", password);
        credentials.put("temporary", false);

        user.put("credentials", Collections.singletonList(credentials));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                repons.put("reponse","Creation du user fait avec succee");
                return ResponseEntity.status(HttpStatus.CREATED).body(repons);
            } else {
                repons.put("reponse","failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(repons);
            }
        } catch (Exception e) {
            repons.put("reponse","failed to create user");
            repons.put("status",HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(repons);
        }
    }
}



