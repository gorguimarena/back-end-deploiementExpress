package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;





import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.MariageDocument;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.NaissanceDocument;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Officier;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.OfficierRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util.PDFUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {
    @Autowired
    private OfficierRepository officierRepository;
    @Autowired
            private UtilisateurService utilisateurService;
    LocalDate today = LocalDate.now();
    String formattedDate = today.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH));


    public byte[] generatePdf(Demande demande, Object documentInfos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Création d'une police pour les titres
            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Ajouter les informations en haut à gauche
            Paragraph header = new Paragraph();
            header.add(new Paragraph(demande.getMairie().getRegion(), fontHeader));
            header.add(new Paragraph(demande.getMairie().getDepartement(), fontHeader));
            header.add(new Paragraph(demande.getMairie().getCommune(), fontHeader));
            header.add(new Paragraph(demande.getMairie().getNom(), fontHeader));


            // Aligner le texte à gauche
            header.setAlignment(Element.ALIGN_LEFT);

            // Ajouter le header au document
            document.add(header);


            // Police pour le titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

// Ajouter le titre centré
            Paragraph title1 = new Paragraph("RÉPUBLIQUE DU SÉNÉGAL", titleFont);
            Paragraph title2 = new Paragraph("Un peulple- Un but- Une foi");
            Paragraph title3 = new Paragraph("ETAT-CIVIL", titleFont);
            title1.setAlignment(Element.ALIGN_CENTER);
            title2.setAlignment(Element.ALIGN_CENTER);
            title3.setAlignment(Element.ALIGN_CENTER);
            document.add(title1);
            document.add(title2);
            document.add(title3);

// Ajouter des informations alignées à droite du titre
            PdfWriter writer = null;
            PdfContentByte canvas = writer.getDirectContent();
            String rightHeader = "Informations supplémentaires ici"; // Exemple d'informations
            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT,                // Alignement à droite
                    new Phrase(rightHeader, normalFont), // Texte
                    550, 820,                           // Coordonnées en haut à droite
                    0                                   // Rotation
            );



            LineSeparator line = new LineSeparator();
            line.setLineWidth(1);
            line.setPercentage(100);
            document.add(line);




            switch (demande.getTypeDocument()) {
                case "extrait_de_naissance":
                    NaissanceDocument extraitNaissance = (NaissanceDocument) documentInfos;
                    document.add(new Paragraph("EXTRAIT DU REGISTRE DES ACTES DE NAISSANCE", titleFont));

// Informations générales
                    document.add(new Paragraph("Pour l'année : " + extraitNaissance.getAnLettre()));
                    document.add(new Paragraph("Numéro : " + extraitNaissance.getNumRegistreLettre()));
                    document.add(new Paragraph("An : " + extraitNaissance.getAnNumero()));
                    document.add(new Paragraph(extraitNaissance.getNumeroRegistre() + " N dans les registres en chiffres"));

// Informations sur la personne
                    document.add(new Paragraph("Prénom : " + extraitNaissance.getPrenom()));
                    document.add(new Paragraph("Nom : " + extraitNaissance.getNom()));
                    document.add(new Paragraph("Sexe : " + extraitNaissance.getSexe()));
                    document.add(new Paragraph("Lieu de naissance : " + extraitNaissance.getLieuNaissance()));
                    document.add(new Paragraph("Date de naissance : " + extraitNaissance.getDateNaisanceLettre()));
                    document.add(new Paragraph("Heure de naissance : " + extraitNaissance.getHeureNaissancelettre()));

// Informations sur les parents
                    document.add(new Paragraph("Nom du père : " + extraitNaissance.getPrenomPere()));
                    document.add(new Paragraph("Nom de la mère : " + extraitNaissance.getPrenomMere() + " " + extraitNaissance.getNomMere()));

// Informations sur le jugement, si disponible
                    if (extraitNaissance.getNumJugement() != null) {
                        document.add(new Paragraph("Numéro de jugement : " + extraitNaissance.getNumJugement()));
                    }

                    break;

                case "copie_litterale_d_acte_de_naissance":
                    NaissanceDocument copieLitterale = (NaissanceDocument) documentInfos;
                    // Titre principal
                    document.add(new Paragraph("COPIE LITTÉRALE DE L'ACTE DE NAISSANCE", titleFont));
                    document.add(new Paragraph("Pour l'année : " + copieLitterale.getAnLettre(), normalFont));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Informations principales
                    document.add(new Paragraph("Numéro : " + copieLitterale.getNumRegistreLettre()));
                    document.add(new Paragraph("Année : " + copieLitterale.getAnNumero()));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Informations sur la naissance
                    document.add(new Paragraph("Naissance de : " + copieLitterale.getPrenom() + " " + copieLitterale.getNom()));
                    document.add(new Paragraph("Sexe : " + copieLitterale.getSexe()));
                    document.add(new Paragraph("Lieu de naissance : " + copieLitterale.getLieuNaissance()));
                    document.add(new Paragraph("Date de naissance : " + copieLitterale.getDateNaisanceLettre()));
                    document.add(new Paragraph("Heure de naissance : " + copieLitterale.getHeureNaissancelettre()));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Informations parentales
                    document.add(new Paragraph("Prénom du père : " + copieLitterale.getPrenomPere()));
                    document.add(new Paragraph("Prénom et nom de la mère : " + copieLitterale.getPrenomMere() + " " + copieLitterale.getNomMere()));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Officier et date de rédaction
                    document.add(new Paragraph("Dressé le : " + formattedDate));
                    document.add(new Paragraph("À " + demande.getMairie().getDepartement() + ", par nous, officier de l'État-Civil."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Mention et validation
                    document.add(new Paragraph("Mention magistrale : " ));
                    document.add(new Paragraph("Pour copie conforme."));
                    document.add(new Paragraph("Fait à : " + demande.getMairie().getRegion() ));
                    document.add(new Paragraph("\n")); // Saut de ligne



                    break;

                case "certificat_de_mariage_constante":
                    MariageDocument copieIntegraleMariage = (MariageDocument) documentInfos;
                    // Titre principal
                    document.add(new Paragraph("CERTIFICAT DE MARIAGE CONSTATÉ", titleFont));
                    document.add(new Paragraph("Centre de l'État-Civil", normalFont));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Numéro d'acte
                    document.add(new Paragraph("Acte N° : " + copieIntegraleMariage.getNumeroActeMariage()));
                    document.add(new Paragraph("NOUS :"));
                    //document.add(new Paragraph(copieIntegraleMariage.getNomOfficier() + ", officier de l'état civil."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Déclaration
                    document.add(new Paragraph("Certifions que :"));
                    document.add(new Paragraph(copieIntegraleMariage.getPrenomEpoux() + " " + copieIntegraleMariage.getNomEpoux() + " et " +
                            copieIntegraleMariage.getPrenomEpouse() + " " + copieIntegraleMariage.getNomEpouse()));
                    document.add(new Paragraph("Ont contracté mariage entre eux selon la coutume."));
                    document.add(new Paragraph("Et que ce mariage a été enregistré par nous."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Régime matrimonial
                    document.add(new Paragraph("L'époux susnommé nous déclare opter pour : " + copieIntegraleMariage.getOpter() + "."));
                    document.add(new Paragraph("Les époux sont convenus du versement d'une dote comprenant :"));
                    document.add(new Paragraph(copieIntegraleMariage.getDote() != null ? copieIntegraleMariage.getDote() + " FCFA." : "Aucune dote spécifiée."));
                    document.add(new Paragraph("Et déclarent opter pour le régime matrimonial : " + copieIntegraleMariage.getRegimeMatrimonial() + "."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Validation et date
                    document.add(new Paragraph("En foi de quoi, nous avons délivré le présent pour servir et valoir ce que de droit."));
                    document.add(new Paragraph("Fait à " + demande.getMairie().getRegion() + ", le " + copieIntegraleMariage.getDate() + "."));
                    document.add(new Paragraph("\n")); // Saut de ligne


                    break;

                case "copie_litterale_acte_de_mariage":
                    MariageDocument acteMariage = (MariageDocument) documentInfos;
                    // Titre principal
                    document.add(new Paragraph("ACTE DE MARIAGE CONSTANT", titleFont));
                    document.add(new Paragraph("Numéro d'acte : " + acteMariage.getNumeroActeMariage()));
                    document.add(new Paragraph("Date de mariage : " + acteMariage.getDate() + " (" + acteMariage.getDatemary() + ")"));
                    document.add(new Paragraph("Régime matrimonial : " + acteMariage.getRegimeMatrimonial()));
                    document.add(new Paragraph("Dot : " + (acteMariage.getDote() != null ? acteMariage.getDote() + " FCFA" : "Non spécifiée")));
                    document.add(new Paragraph("Option : " + acteMariage.getOpter()));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Informations sur l'ÉPOUX
                    document.add(new Paragraph("L'époux :"));
                    document.add(new Paragraph(acteMariage.getPrenomEpoux() + " " + acteMariage.getNomEpoux() +
                            ", né le " + acteMariage.getNaissanceEpoux() + " à " + acteMariage.getLieunaissanceEpoux() + ","));
                    document.add(new Paragraph("domicilié à " + acteMariage.getDomicileEpoux() + ", en résidence à " + acteMariage.getResidenceEpoux() + ","));
                    document.add(new Paragraph("exerçant la profession de " + acteMariage.getProfessionEpoux() + ","));
                    document.add(new Paragraph("fils de " + acteMariage.getNomparentepoux() + ", né(e) le " + acteMariage.getNaissanceparentepoux() +
                            " à " + acteMariage.getLieunaissanceparentepoux() + ","));
                    document.add(new Paragraph("exerçant la profession de " + acteMariage.getProfessionparentepoux() +
                            " et domicilié à " + acteMariage.getDomicileparent() + "."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Informations sur l'ÉPOUSE
                    document.add(new Paragraph("L'épouse :"));
                    document.add(new Paragraph(acteMariage.getPrenomEpouse() + " " + acteMariage.getNomEpouse() +
                            ", née le " + acteMariage.getNaissanceEpouse() + " à " + acteMariage.getLieunaissanceEpouse() + ","));
                    document.add(new Paragraph("domiciliée à " + acteMariage.getDomicileEpouse() + ", en résidence à " + acteMariage.getResidenceEpouse() + ","));
                    document.add(new Paragraph("exerçant la profession de " + acteMariage.getProfessionEpouse() + ","));
                    document.add(new Paragraph("fille de " + acteMariage.getNomparentepouse() + ", né(e) le " + acteMariage.getNaissanceparentepouse() +
                            " à " + acteMariage.getLieunaissanceparentepouse() + ","));
                    document.add(new Paragraph("exerçant la profession de " + acteMariage.getProfessionparentepouse() +
                            " et domiciliée à " + acteMariage.getDomicileparentepouse() + "."));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Témoins
                    document.add(new Paragraph("Les témoins :"));
                    document.add(new Paragraph("1. " + acteMariage.getPremiertemoin()));
                    document.add(new Paragraph("2. " + acteMariage.getDeuxiemetemoin()));
                    document.add(new Paragraph("\n")); // Saut de ligne

// Fin du document
                    document.add(new Paragraph("En foi de quoi, nous avons procédé à l'enregistrement de cet acte de mariage.", normalFont));

                    break;






                default:
                    throw new IllegalArgumentException("Type de document inconnu: " + demande.getTypeDocument());
            }
            //signature de l'officier----------------------------------------------------
            Integer officierId=demande.getMairie().getId();
            byte[] signatureBytes = utilisateurService.getSignature(officierId);
            Officier officier = officierRepository.findById(officierId).orElseThrow(() -> new RuntimeException("Officier not found"));

            Image signatureImage = Image.getInstance(signatureBytes);
            signatureImage.scaleToFit(150, 50); // Ajustez la taille de l'image si nécessaire
            signatureImage.setAlignment(Image.ALIGN_RIGHT);
            document.add(new Paragraph("\n"));
            document.add(signatureImage);
            Paragraph officierName = new Paragraph("Officier de l'État-Civil : " + officier.getNom() + " " + officier.getPrenom(),titleFont);
            officierName.setAlignment(Element.ALIGN_RIGHT);
            document.add(officierName);
           //-----------------------------------------------------------------------------
            // Dans votre méthode generatePdf
            PDFUtils.generatePdfWithQRCode(demande, documentInfos);



            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
