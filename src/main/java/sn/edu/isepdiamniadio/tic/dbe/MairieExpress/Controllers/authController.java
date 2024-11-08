package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.AuthService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestAuth;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestLogout;


import java.util.Map;

@RestController
@RequestMapping("/api/")
public class authController {

    @Autowired
    private AuthService authService;

    @PostMapping("auth")
    public ResponseEntity<Map<String, Object>> login(@RequestBody RequestAuth requestAuth) {
        return authService.authenticate(requestAuth.getUsername(), requestAuth.getPassword());
    }
    @GetMapping("logout/{username}")
    public ResponseEntity<?> logout(@PathVariable String username) {
        return authService.logoutUser(username);
    }
}
