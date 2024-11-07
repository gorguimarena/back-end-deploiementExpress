package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.AuthService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.KeycloakService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestRegister;


import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class RegisterController {

    @Autowired
    private AuthService authService;

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RequestRegister r){
        return keycloakService.creerUser(r.getEmail(),r.getPassword(),r.getNom(),r.getPrenom(),r.getUsername(),r.getRole());
    }



}
