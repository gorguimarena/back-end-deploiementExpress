package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.DemandeRequest;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.DemandeService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.CitoyenService;

import java.util.List;


@RestController
@RequestMapping("/demande")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private CitoyenService citoyenService;

    // Endpoint pour créer une demande
    @PostMapping
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest demandeRequest, @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            demandeService.createDemande(demandeRequest, tokenValue);
            return ResponseEntity.ok("Demande créée avec succès et en attente de validation.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint pour valider une demande par un agent
    @PostMapping("/{id}/valider")
    public ResponseEntity<?> validerDemande(@PathVariable Long id) {
        try {
            demandeService.validerDemande(id);
            return ResponseEntity.ok("Demande validée et PDF envoyé.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint pour récupérer les demandes par mairie
    @GetMapping("/mairie/{mairieId}")
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
