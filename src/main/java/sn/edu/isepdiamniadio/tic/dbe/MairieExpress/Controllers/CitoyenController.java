package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.KeycloakService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestRegister;

@RestController
@RequestMapping("/api/")
public class CitoyenController {

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("registe")
    public ResponseEntity<?> registerCitoyen(@RequestBody RequestRegister r) {
        return keycloakService.creerUser(r.getUsername(),r.getPassword(),r.getPrenom(),r.getNom(),r.getEmail(),r.getRole());
    }
}
