package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Document;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DemandeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DocumentEnvoyeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MariageDocumentRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.NaissanceDocumentRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DocumentEnvoyeService {

    private final DemandeRepository demandeRepository;
    private final DocumentEnvoyeRepository documentEnvoyeRepository;
    private final MariageDocumentRepository mariageDocumentRepository;
    private final NaissanceDocumentRepository naissanceDocumentRepository;

    // Méthode pour valider une demande et envoyer le document
    public DocumentEnvoye validerDemande(Long demandeId) {
        // Récupérer la demande par son ID
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // Vérifier quel type de document est lié à cette demande et le récupérer
        Document document = null;
        if ("EXTRAIT".equals(demande.getTypeDocument())) {
            // Récupérer le document associé à l'extrait de naissance
            document = naissanceDocumentRepository.findByDemande(demande);
        } else if ("MARIAGE".equals(demande.getTypeDocument())) {
            // Récupérer le document associé à l'acte de mariage
            document = mariageDocumentRepository.findByDemande(demande);
        }

        // Si aucun document n'est trouvé
        if (document == null) {
            throw new RuntimeException("Le document demandé n'existe pas pour cette demande.");
        }

        // Créer une nouvelle instance de DocumentEnvoye pour enregistrer le document envoyé
        DocumentEnvoye documentEnvoye = new DocumentEnvoye();
        documentEnvoye.setTypeDocument(demande.getTypeDocument()); // Le type du document
        documentEnvoye.setPathDocument(generatePdf(document)); // Générer le PDF et obtenir son chemin
        documentEnvoye.setDateEnvoi(new Date()); // Date actuelle de l'envoi
        documentEnvoye.setDemande(demande); // Lier à la demande correspondante

        // Sauvegarder l'enregistrement du document envoyé dans la base de données
        documentEnvoyeRepository.save(documentEnvoye);

        // Mettre à jour le statut de la demande à "VALIDEE"
        demande.setStatutDemande("VALIDEE");
        demandeRepository.save(demande);

        return documentEnvoye; // Retourner l'objet DocumentEnvoye créé
    }

    // Méthode pour générer un fichier PDF (exemple simple de chemin)
    private String generatePdf(Document document) {
        // Logic to generate PDF file (use iText, PDFBox, or another library)
        // For now, return a mock path as an example
        return "/path/to/generated/pdf/" + document.getId() + ".pdf"; // Exemple de chemin
    }
}

