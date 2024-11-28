package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.DemandeService;

import java.util.List;

@RestController
@RequestMapping("/demande")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    /**
     * Endpoint pour créer une demande
     * @param mairieId ID de la mairie
     * @param typeDocument Type de document demandé
     * @param numeroRegistre Numéro de registre (si extrait de naissance)
     * @param numeroActeMariage Numéro d'acte de mariage (si certificat de mariage)
     * @param prenomInteresse Prénom de l'intéressé (pour naissance)
     * @param nomInteresse Nom de l'intéressé (pour naissance)
     * @param prenomEpoux Prénom de l'époux (pour mariage)
     * @param nomEpoux Nom de l'époux (pour mariage)
     * @param prenomEpouse Prénom de l'épouse (pour mariage)
     * @param nomEpouse Nom de l'épouse (pour mariage)
     * @param citoyenId ID du citoyen qui fait la demande
     * @return La demande créée
     * @throws Exception Si la création échoue
     */
    @PostMapping("/create")
    public ResponseEntity<Demande> createDemande(@RequestParam Integer mairieId,
                                                 @RequestParam String typeDocument,
                                                 @RequestParam(required = false) String numeroRegistre,
                                                 @RequestParam(required = false) String numeroActeMariage,
                                                 @RequestParam(required = false) String prenomInteresse,
                                                 @RequestParam(required = false) String nomInteresse,
                                                 @RequestParam(required = false) String prenomEpoux,
                                                 @RequestParam(required = false) String nomEpoux,
                                                 @RequestParam(required = false) String prenomEpouse,
                                                 @RequestParam(required = false) String nomEpouse,
                                                 @RequestParam Integer citoyenId) throws Exception {

        Demande demande = demandeService.createDemande(mairieId, typeDocument, numeroRegistre, numeroActeMariage,
                prenomInteresse, nomInteresse, prenomEpoux, nomEpoux,
                prenomEpouse, nomEpouse, citoyenId);

        return ResponseEntity.ok(demande);
    }

    /**
     * Endpoint pour récupérer toutes les demandes d'une mairie
     * @param mairieId ID de la mairie
     * @return Liste des demandes pour cette mairie
     */
    @GetMapping("/all/{mairieId}")
    public ResponseEntity<List<Demande>> getAllDemandeForMairie(@PathVariable Integer mairieId) {
        List<Demande> demandes = demandeService.getAllDemandeForMairie(mairieId);
        return ResponseEntity.ok(demandes);
    }

    /**
     * Endpoint pour valider une demande
     * @param demandeId ID de la demande à valider
     * @param officierId ID de l'officier qui valide la demande
     * @return La demande mise à jour avec statut validé et PDF associé
     * @throws Exception Si la validation échoue
     */
    @PostMapping("/validate/{demandeId}")
    public ResponseEntity<Demande> validateDemande(@PathVariable Long demandeId, @RequestParam Long officierId) throws Exception {
        Demande validatedDemande = demandeService.validateDemande(demandeId, officierId);
        return ResponseEntity.ok(validatedDemande);
    }

    /**
     * Endpoint pour rejeter une demande
     * @param demandeId ID de la demande à rejeter
     * @return La demande mise à jour avec statut rejeté
     * @throws Exception Si le rejet échoue
     */
    @PostMapping("/reject/{demandeId}")
    public ResponseEntity<Demande> rejectDemande(@PathVariable Long demandeId) throws Exception {
        Demande rejectedDemande = demandeService.rejectDemande(demandeId);
        return ResponseEntity.ok(rejectedDemande);
    }
}
