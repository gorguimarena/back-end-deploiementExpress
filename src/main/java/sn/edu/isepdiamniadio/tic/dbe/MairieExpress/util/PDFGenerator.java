package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util;




import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Officier;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.OfficierRepository;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PDFGenerator {

    @Autowired
    private OfficierRepository officierRepository;  // Repository pour l'Officier
    @Autowired
    private MairieRepository mairieRepository;  // Repository pour la Mairie

    public String generatePDF(Demande demande) throws IOException, DocumentException {
        // Récupérer la mairie de la demande
        Integer mairieId = demande.getMairie().getId();
        Mairie mairie = mairieRepository.findById(mairieId)
                .orElseThrow(() -> new RuntimeException("Mairie non trouvée"));

        // Trouver l'officier associé à la mairie
        Officier officier = officierRepository.findByMairie(mairie)
                .orElseThrow(() -> new RuntimeException("Officier non trouvé pour cette mairie"));

        // Récupérer la signature de l'officier
        byte[] signatureBytes = officier.getSignature();
        if (signatureBytes == null) {
            throw new RuntimeException("L'officier n'a pas de signature enregistrée");
        }

        // Créer un document PDF
        Document document = new Document();
        String fileName = "document_" + demande.getId() + ".pdf";
        try (FileOutputStream fileOutputStream = new FileOutputStream("path/to/save/" + fileName)) {
            PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            // Ajouter un titre au PDF
            document.add(new Paragraph("Demande de " + demande.getTypeDocument()));

            // Ajouter les détails de la demande
            document.add(new Paragraph("Nom du citoyen : " + demande.getNomInteresse() + " " + demande.getPrenomInteresse()));
            document.add(new Paragraph("Date de la demande : " + demande.getDateDemande()));

            // Ajouter des informations spécifiques sur le document
            if ("extrait_de_naissance".equals(demande.getTypeDocument()) || "copie_litterale_de_naissance".equals(demande.getTypeDocument())) {
                document.add(new Paragraph("Numéro registre : " + demande.getNumeroRegistre()));
            } else if ("certificat_mariage".equals(demande.getTypeDocument()) || "copie_litterale_mariage".equals(demande.getTypeDocument())) {
                document.add(new Paragraph("Numéro acte mariage : " + demande.getNumeroActeMariage()));
            }

            // Ajouter la signature de l'officier (récupérer l'image en bytes et la convertir en image)
            Image signatureImage = Image.getInstance(signatureBytes); // Utilisation du tableau d'octets directement
            signatureImage.scaleToFit(100f, 50f);  // Ajuster la taille de l'image
            document.add(signatureImage);  // Ajouter la signature à la fin du document

            // Fermer le document PDF
            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new IOException("Erreur lors de la génération du PDF", e);
        }

        // Retourner le chemin du fichier généré
        return fileName;
    }
}
