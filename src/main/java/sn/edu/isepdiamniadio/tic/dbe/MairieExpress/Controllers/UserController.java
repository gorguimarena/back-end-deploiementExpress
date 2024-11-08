package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.KeycloakService;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private KeycloakService keycloakService;

    @GetMapping("{username}")
    public ResponseEntity<?> getUser(@PathVariable String username){
        return keycloakService.user(username);
    }
}
