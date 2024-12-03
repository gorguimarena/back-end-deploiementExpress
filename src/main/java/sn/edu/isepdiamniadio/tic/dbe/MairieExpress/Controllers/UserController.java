package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.AdminMairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Utilisateur;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.UtilisateurService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getIdMairie(@PathVariable Integer id) {
        return utilisateurService.getIdMairie(id);
    }

    @GetMapping
    public List<Utilisateur> getAllMairie() {
        return utilisateurService.getAllPersMairie();
    }

    @PostMapping("/{id}/signature")
    public ResponseEntity<?> saveSignature(@PathVariable Integer id, @RequestParam("signature") MultipartFile file) {
        try {
            utilisateurService.saveSignature(id, file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/signature")
    public ResponseEntity<?> getSignature(@PathVariable Integer id) {
        try {
            byte[] signature = utilisateurService.getSignature(id);
            return ResponseEntity.ok().body(signature);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Signature not found");
        }
    }

    @GetMapping("/admin-mairies")
    public List<AdminMairie> getAllUser() {
        return utilisateurService.getAdminMairies();
    }
    @GetMapping("/users-mairie/{idMairie}")
    public List<?> getUserMairie(@PathVariable Integer idMairie) {
        return utilisateurService.getUsersMairie(idMairie);
    }
}
