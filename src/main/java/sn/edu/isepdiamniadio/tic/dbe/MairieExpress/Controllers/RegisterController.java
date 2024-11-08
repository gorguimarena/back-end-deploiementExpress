package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import jakarta.ws.rs.core.Response;
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

    @PostMapping("registe")
    public void register(@RequestBody RequestRegister r){
        keycloakService.creerUser(r.getUsername(),r.getPassword(),r.getPrenom(),r.getNom(),r.getEmail(),r.getRole());
    }



}
