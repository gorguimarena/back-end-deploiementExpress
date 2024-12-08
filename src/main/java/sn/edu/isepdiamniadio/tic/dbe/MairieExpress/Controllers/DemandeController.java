package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DemandeRequest;
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
    @PostMapping("/citoyen")
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest demandeRequest, @AuthenticationPrincipal Jwt jwt) {
        try {
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
            return ResponseEntity.ok("Demande créée avec succès et en attente de validation.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // Endpoint pour valider une demande par un agent
   /* @PostMapping("/agent/{id}/valider")
    public ResponseEntity<?> validerDemande(@PathVariable Long id) {
        try {
            demandeService.validerDemande(id);
            return ResponseEntity.ok("Demande validée et PDF envoyé.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    */

    @PostMapping("/agent/{id}/valider")
    public ResponseEntity<?> validerDemande(@PathVariable Long id) {
        try {
            // Appel du service pour valider la demande et générer le PDF
            String pdfUrl = demandeService.validerDemande(id);

            // Répondre avec succès et fournir l'URL générée
            return ResponseEntity.ok("Demande validée et PDF envoyé. URL : " + pdfUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la validation de la demande : " + e.getMessage());
        }
    }

    // Endpoint pour récupérer les demandes par mairie
    @GetMapping("/agent/mairie/{mairieId}")
    public ResponseEntity<List<Demande>> getDemandesByMairie(@PathVariable Integer mairieId) {
        List<Demande> demandes = demandeService.getDemandesByMairie(mairieId);
        return ResponseEntity.ok(demandes);
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
