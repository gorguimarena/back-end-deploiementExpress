package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.AuthService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestAuth;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.RequestLogout;


import java.util.Map;

@RestController
@RequestMapping("/api/")
public class authController {

    @Autowired
    private AuthService authService;

    @PostMapping("token")
    public ResponseEntity<Map<String, Object>> login(@RequestBody RequestAuth requestAuth) {
        return authService.authenticate(requestAuth.getUsername(), requestAuth.getPassword());
    }

    @PostMapping("logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody RequestLogout requestLogout) {
        return authService.logout(requestLogout.getToken());
    }
}
