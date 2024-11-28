package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util.PDFGenerator;

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
    private MairieRepository mairieRepository;

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Autowired
    private PDFGenerator pdfGenerator;

    public Demande createDemande(Integer mairieId, String typeDocument, String numeroRegistre,
                                 String numeroActeMariage, String prenomInteresse, String nomInteresse,
                                 String prenomEpoux, String nomEpoux, String prenomEpouse, String nomEpouse,
                                 Integer citoyenId) throws Exception {

        // Récupération de la mairie et du citoyen
        Optional<Mairie> mairieOpt = mairieRepository.findById(mairieId);
        Optional<Citoyen> citoyenOpt = citoyenRepository.findById(citoyenId);

        if (mairieOpt.isEmpty()) {
            throw new Exception("Mairie non trouvée");
        }
        if (citoyenOpt.isEmpty()) {
            throw new Exception("Citoyen non trouvé");
        }

        Mairie mairie = mairieOpt.get();
        Citoyen citoyen = citoyenOpt.get();

        // Création de la demande
        Demande demande = new Demande();
        demande.setMairie(mairie);
        demande.setCitoyen(citoyen);
        demande.setTypeDocument(typeDocument);
        demande.setDateDemande(new java.util.Date());
        demande.setStatutDemande("en attente");

        // Vérification des informations et ajout des données spécifiques au type de document
        if ("extrait_de_naissance".equals(typeDocument) || "copie_litterale_de_naissance".equals(typeDocument)) {
            // Vérification dans la table NaissanceDocument
            Optional<NaissanceDocument> naissanceDocument = naissanceDocumentRepository
                    .findByNumeroRegistreAndNomAndPrenom(numeroRegistre, nomInteresse, prenomInteresse);

            if (naissanceDocument.isEmpty()) {
                throw new Exception("Les informations de naissance ne correspondent à aucun enregistrement.");
            }

            // Ajout des données à la demande
            demande.setNumeroRegistre(numeroRegistre);
            demande.setPrenomInteresse(prenomInteresse);
            demande.setNomInteresse(nomInteresse);
        } else if ("certificat_mariage".equals(typeDocument) || "copie_litterale_mariage".equals(typeDocument)) {
            // Vérification dans la table MariageDocument
            Optional<MariageDocument> mariageDocument = mariageDocumentRepository
                    .findByNumeroActeMariageAndNomEpouxAndPrenomEpouxAndNomEpouseAndPrenomEpouse(
                            numeroActeMariage, nomEpoux, prenomEpoux, nomEpouse, prenomEpouse);

            if (mariageDocument.isEmpty()) {
                throw new Exception("Les informations de mariage ne correspondent à aucun enregistrement.");
            }

            // Ajout des données à la demande
            demande.setNumeroActeMariage(numeroActeMariage);
            demande.setPrenomepoux(prenomEpoux);
            demande.setNomepoux(nomEpoux);
            demande.setPrenomepouse(prenomEpouse);
            demande.setNomepouse(nomEpouse);
        }

        // Sauvegarde de la demande avant génération du PDF
        Demande savedDemande = demandeRepository.save(demande);

        // Génération du PDF associé à la demande
        String pdfPath = pdfGenerator.generatePDF(savedDemande);

        // Assurez-vous que le PDF a bien été généré avant de mettre à jour la demande
        if (pdfPath != null && !pdfPath.isEmpty()) {
            savedDemande.setPdfUrl(pdfPath);
            // Sauvegarde de l'URL du PDF dans la base de données
            savedDemande = demandeRepository.save(savedDemande);
        } else {
            throw new Exception("Erreur lors de la génération du PDF.");
        }

        return savedDemande;
    }

    public List<Demande> getAllDemandeForMairie(Integer mairieId) {
        return demandeRepository.findByMairieId(mairieId);
    }

    /**
     * Valide une demande en générant un PDF
     *
     * @param demandeId  ID de la demande à valider
     * @param officierId
     * @return La demande validée
     * @throws Exception Si la validation échoue
     */
    public Demande validateDemande(Long demandeId, Long officierId) throws Exception {
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);

        if (demandeOpt.isEmpty()) {
            throw new Exception("Demande non trouvée");
        }

        Demande demande = demandeOpt.get();
        demande.setStatutDemande("validée");

        // Générer le PDF
        String pdfPath = pdfGenerator.generatePDF(demande);
        demande.setPdfUrl(pdfPath);

        return demandeRepository.save(demande);
    }

    public Demande rejectDemande(Long demandeId) throws Exception {
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);

        if (demandeOpt.isEmpty()) {
            throw new Exception("Demande non trouvée");
        }

        Demande demande = demandeOpt.get();
        demande.setStatutDemande("rejetée");

        return demandeRepository.save(demande);
    }

}
