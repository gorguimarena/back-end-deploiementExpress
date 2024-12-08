package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.DemandeRequest;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.DemandeService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.CitoyenService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.CitoyenRepository;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/demande")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private CitoyenService citoyenService;
    @Autowired
    private CitoyenRepository citoyenRepository;

    // Endpoint pour créer une demande
    @PostMapping(value = "/citoyen", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest demandeRequest, @AuthenticationPrincipal Jwt jwt) {
        try {

            System.out.println(jwt.getClaims());
            // Extraire l'adresse e-mail du token
            String email = jwt.getClaim("email");

            // Rechercher le citoyen par e-mail
            Optional<Citoyen> citoyenOpt = citoyenRepository.findByEmail(email);
            if (citoyenOpt.isEmpty()) {
                throw new IllegalArgumentException("Citoyen non trouvé ou token invalide.");
            }
            Citoyen citoyen = citoyenOpt.get();

            // Créer la demande
            demandeService.createDemande(demandeRequest, citoyen);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Endpoint pour valider une demande par un agent
    @PostMapping("/agent/{id}/valider")
    public ResponseEntity<?> validerDemande(@PathVariable Long id) {
        try {
            demandeService.validerDemande(id);
            return ResponseEntity.ok("Demande validée et PDF envoyé.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint pour récupérer les demandes par mairie
    @GetMapping("/agent/mairie/{mairieId}")
    public ResponseEntity<List<Demande>> getDemandesByMairie(@PathVariable Integer mairieId) {
        List<Demande> demandes = demandeService.getDemandesByMairie(mairieId);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/citoyen/{id}")
    public ResponseEntity<List<Demande>> getDemandeByCitoyen(@PathVariable Integer id) {
        return citoyenService.getDemandes(id);
    }
    // Endpoint pour obtenir les documents envoyés à un citoyen
    @GetMapping("/citoyen/{id}/documents")
    public ResponseEntity<?> getDocumentsEnvoyes(@PathVariable Integer id) {
        try {
            List<DocumentEnvoye> documentsEnvoyes = citoyenService.getDocumentsEnvoyes(id);
            return ResponseEntity.ok(documentsEnvoyes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
