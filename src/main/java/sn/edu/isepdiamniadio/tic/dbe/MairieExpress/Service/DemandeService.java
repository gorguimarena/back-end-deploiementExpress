package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.DemandeRequest;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DemandeService {
    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private NaissanceDocumentRepository naissanceDocumentRepository;

    @Autowired
    private MariageDocumentRepository mariageDocumentRepository;

    @Autowired
    private DocumentEnvoyeRepository documentEnvoyeRepository;

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Autowired
    private PdfService pdfService;

    public boolean checkDocumentExists(DemandeRequest demandeRequest) {
        if ("extrait_de_naissance".equals(demandeRequest.getTypeDocument()) || "copie_litterale_d_acte_de_naissance".equals(demandeRequest.getTypeDocument())) {
            return naissanceDocumentRepository.existsByNumeroRegistreAndAnNumeroAndPrenomAndNom(
                    demandeRequest.getNumeroRegistre(), demandeRequest.getAnNumero(), demandeRequest.getPrenom(), demandeRequest.getNom());
        } else if ("certificat_de_mariage_constante".equals(demandeRequest.getTypeDocument()) || "copie_litterale_acte_de_mariage".equals(demandeRequest.getTypeDocument())) {
            return mariageDocumentRepository.existsByNumeroActeMariageAndPrenomEpouxAndNomEpouxAndPrenomEpouseAndNomEpouse(
                    demandeRequest.getNumeroActeMariage(), demandeRequest.getPrenomEpoux(), demandeRequest.getNomEpoux(), demandeRequest.getPrenomEpouse(), demandeRequest.getNomEpouse());
        }
        return false;
    }

    public void createDemande(DemandeRequest demandeRequest, String token) {
        Optional<Citoyen> citoyenOpt = citoyenRepository.findByToken(token);
        if (citoyenOpt.isEmpty()) {
            throw new IllegalArgumentException("Citoyen non trouvé ou token invalide.");
        }
        Citoyen citoyen = citoyenOpt.get();

        boolean documentExists = checkDocumentExists(demandeRequest);
        if (documentExists) {
            Demande demande = new Demande();
            demande.setCitoyen(citoyen);
            demande.setMairie(new Mairie(demandeRequest.getMairieId()));
            demande.setTypeDocument(demandeRequest.getTypeDocument());
            demande.setStatutDemande("en attente");
            demande.setDateDemande(new Date());

            // Définir les champs spécifiques
            if ("extrait_de_naissance".equals(demandeRequest.getTypeDocument()) || "copie_litterale_d_acte_de_naissance".equals(demandeRequest.getTypeDocument())) {
                demande.setNumeroRegistre(demandeRequest.getNumeroRegistre());
                demande.setPrenomInteresse(demandeRequest.getPrenom());
                demande.setNomInteresse(demandeRequest.getNom());
                demande.setAnNumero(demandeRequest.getAnNumero());
            } else if ("certificat_de_mariage_constante".equals(demandeRequest.getTypeDocument()) || "copie_litterale_acte_de_mariage".equals(demandeRequest.getTypeDocument())) {
                demande.setNumeroActeMariage(demandeRequest.getNumeroActeMariage());
                demande.setPrenomepoux(demandeRequest.getPrenomEpoux());
                demande.setNomepoux(demandeRequest.getNomEpoux());
                demande.setPrenomepouse(demandeRequest.getPrenomEpouse());
                demande.setNomepouse(demandeRequest.getNomEpouse());
                demande.setDatemary(demandeRequest.getDatemary());
            }

            demandeRepository.save(demande);
        } else {
            throw new IllegalArgumentException("Les informations fournies ne sont pas valides.");
        }
    }

    public void validerDemande(Long id) {
        Optional<Demande> demandeOpt = demandeRepository.findById(id);
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            demande.setStatutDemande("validé");
            demandeRepository.save(demande);

            Object documentInfos = null;
            if ("extrait_de_naissance".equals(demande.getTypeDocument()) || "copie_litterale_d_acte_de_naissance".equals(demande.getTypeDocument())) {
                documentInfos = naissanceDocumentRepository.findByNumeroRegistreAndAnNumeroAndPrenomAndNom(
                        demande.getNumeroRegistre(), demande.getAnNumero(), demande.getPrenomInteresse(), demande.getNomInteresse());
            } else if ("certificat_de_mariage_constante".equals(demande.getTypeDocument()) || "copie_litterale_acte_de_mariage".equals(demande.getTypeDocument())) {
                documentInfos = mariageDocumentRepository.findByNumeroActeMariageAndPrenomEpouxAndNomEpouxAndPrenomEpouseAndNomEpouse(
                        demande.getNumeroActeMariage(), demande.getPrenomepoux(), demande.getNomepoux(), demande.getPrenomepouse(), demande.getNomepouse());
            }

            byte[] pdfData = pdfService.generatePdf(demande, documentInfos);
            DocumentEnvoye documentEnvoye = new DocumentEnvoye();
            documentEnvoye.setDemande(demande);
            documentEnvoye.setCitoyen(demande.getCitoyen());
            documentEnvoye.setMairie(demande.getMairie());
            documentEnvoye.setPdfUrl("URL du PDF généré");
            documentEnvoyeRepository.save(documentEnvoye);
        } else {
            throw new IllegalArgumentException("Demande non trouvée.");
        }
    }

    public List<Demande> getDemandesByMairie(Integer mairieId) {
        return demandeRepository.findByMairieId(mairieId);
    }
}
