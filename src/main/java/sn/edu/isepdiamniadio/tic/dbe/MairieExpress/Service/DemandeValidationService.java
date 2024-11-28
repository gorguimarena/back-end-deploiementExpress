package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DemandeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DocumentEnvoyeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util.PDFGenerator;


import java.util.List;

@Service
public class DemandeValidationService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private PDFGenerator pdfGenerator;

    @Autowired
    private DocumentEnvoyeRepository documentEnvoyeRepository;

    @Autowired
    private MairieRepository mairieRepository;

    public Demande createDemande(Mairie mairie, String typeDocument, String numeroRegistre, String numeroActeMariage,
                                 String prenomInteresse, String nomInteresse, String prenomEpoux, String nomEpoux,
                                 String prenomEpouse, String nomEpouse, Citoyen citoyen) throws Exception {
        Demande demande = new Demande();
        demande.setMairie(mairie);
        demande.setTypeDocument(typeDocument);
        demande.setCitoyen(citoyen);

        // Ajouter des informations spécifiques en fonction du type de document
        if (typeDocument.equals("extrait_de_naissance") || typeDocument.equals("copie_litterale_de_naissance")) {
            demande.setNumeroRegistre(numeroRegistre);
            demande.setNomInteresse(nomInteresse);
            demande.setPrenomInteresse(prenomInteresse);
        } else if (typeDocument.equals("certificat_mariage") || typeDocument.equals("copie_litterale_mariage")) {
            demande.setNumeroActeMariage(numeroActeMariage);
            demande.setPrenomepoux(prenomEpoux);
            demande.setNomepoux(nomEpoux);
            demande.setPrenomepouse(prenomEpouse);
            demande.setNomepouse(nomEpouse);
        }

        // Par défaut, mettre le statut à "en attente"
        demande.setStatutDemande("en attente");

        return demandeRepository.save(demande);
    }

    public List<Demande> getPendingDemandes(Long mairieId) {
        return demandeRepository.findByMairie_IdAndStatutDemande(mairieId, "en attente");
    }

    public Demande validateDemande(Long demandeId, Long agentMairieId) throws Exception {
        Demande demande = demandeRepository.findById(demandeId).orElseThrow(() -> new Exception("Demande non trouvée"));
        if (!demande.getMairie().getId().equals(agentMairieId)) {
            throw new Exception("Cet agent ne gère pas cette demande");
        }

        // Mettre à jour le statut de la demande
        demande.setStatutDemande("validée");

        // Générer le PDF
        String pdfUrl = pdfGenerator.generatePDF(demande);
        demande.setPdfUrl(pdfUrl);

        // Enregistrer la demande validée
        Demande updatedDemande = demandeRepository.save(demande);

        // Enregistrer dans DocumentEnvoye
        DocumentEnvoye documentEnvoye = new DocumentEnvoye();
        documentEnvoye.setDemande(updatedDemande);
        documentEnvoye.setCitoyen(updatedDemande.getCitoyen());
        documentEnvoye.setMairie(updatedDemande.getMairie());
        documentEnvoye.setPdfUrl(pdfUrl);
        documentEnvoyeRepository.save(documentEnvoye);

        return updatedDemande;
    }

    public Demande rejectDemande(Long demandeId, Long agentMairieId) throws Exception {
        Demande demande = demandeRepository.findById(demandeId).orElseThrow(() -> new Exception("Demande non trouvée"));
        if (!demande.getMairie().getId().equals(agentMairieId)) {
            throw new Exception("Cet agent ne gère pas cette demande");
        }

        // Mettre à jour le statut de la demande
        demande.setStatutDemande("rejetée");

        return demandeRepository.save(demande);
    }
}
