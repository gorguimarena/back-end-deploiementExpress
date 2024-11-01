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

    @PostMapping("register")
    public ResponseEntity<Map<String,Object>> registerUser(@RequestBody RequestRegister register){
        return authService.createUser(register.getName(),register.getPassword(),register.getEmail(),register.getPhone());
    }

    @PostMapping("infos")
    public ResponseEntity<String> registerUserInfo(@RequestBody String token){
       return keycloakService.getUser(token);
    }

}
