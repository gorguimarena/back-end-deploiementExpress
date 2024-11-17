package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Config.PasswordGenerator;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.KeycloakService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestRegister;



@RestController
@RequestMapping("/api/user/")
public class RegisterController {

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("registe")
    public ResponseEntity<?> register(@RequestBody RequestRegister r){
        String username = keycloakService.getUsernameFromEmail(r.getEmail());
        return keycloakService.createAdmin(r.getEmail(),PasswordGenerator.generateDefaultPassword(),r.getNom(),r.getPrenom(),r.getRole(),r.getIdMirie(),username);
    }

}
