package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KeycloakService {


    private String serverUrl = "http://localhost:8080";

    private String realm = "myRealm";


    private String clientId = "customer1";


    private String clientSecret = "phlyQlUqDQeTaKtwVYeUpm2ntOavZxWo";

    private String grantType = "client_credentials";

    @Autowired
    private RestTemplate restTemplate;


    public ResponseEntity<String> getUser(String token){
        String keycloakUrl = "http://localhost:8080/realms/myRealm/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map[]> response = restTemplate.exchange(
                keycloakUrl,
                HttpMethod.GET,
                request,
                Map[].class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
             response.getBody();
            return ResponseEntity.ok().body(response.getBody().toString());
        } else {
            throw new RuntimeException("Error retrieving users: " + response.getStatusCode());
        }
    }




}
