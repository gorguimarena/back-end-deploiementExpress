package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;





import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.OfficierRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.UtilisateurRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util.PDFUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Service
public class PdfService {

    @Autowired
    private OfficierRepository officierRepository;
    @Autowired
            private UtilisateurService utilisateurService;
    LocalDate today = LocalDate.now();
    String formattedDate = today.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH));
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    public byte[] generatePdf(Demande demande, Object documentInfos)  {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer =PdfWriter.getInstance(document, baos);
            document.open();

            // Création d'une police pour les titres
            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Ajouter les informations en haut à gauche
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Region de "+demande.getMairie().getRegion()));
            header.add(new Paragraph("Departement de "+demande.getMairie().getDepartement()));
            header.add(new Paragraph("Commune de "+demande.getMairie().getCommune()));
            header.add(new Paragraph(demande.getMairie().getNom(), fontHeader));
            // Aligner le texte à gauche
            header.setAlignment(Element.ALIGN_LEFT);
            // Ajouter le header au document
            document.add(header);

            // Police pour le titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph head = new Paragraph();
            Paragraph title1 = new Paragraph("RÉPUBLIQUE DU SÉNÉGAL", titleFont);
            title1.setAlignment(Element.ALIGN_RIGHT);
            head.add(title1);

            Paragraph title2 = new Paragraph("Un peuple- Un but- Une foi");
            title2.setAlignment(Element.ALIGN_RIGHT);
            head.add(title2);

            Paragraph title3 = new Paragraph("ETAT-CIVIL");
            title3.setAlignment(Element.ALIGN_RIGHT);
            head.add(title3);

            document.add(head);




// Ajouter des informations alignées à droite du titre

            PdfContentByte canvas = writer.getDirectContent();
            String rightHeader = ""; // Exemple d'informations
            ColumnText.showTextAligned(
                   canvas,
                   Element.ALIGN_RIGHT,// Alignement à droite
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
                    String paragraphContent = "L'époux : " +
                            acteMariage.getPrenomEpoux() + " " + acteMariage.getNomEpoux() +
                            ", né le " + acteMariage.getNaissanceEpoux() + " à " + acteMariage.getLieunaissanceEpoux() + ", " +
                            "domicilié à " + acteMariage.getDomicileEpoux() + ", en résidence à " + acteMariage.getResidenceEpoux() + ", " +
                            "exerçant la profession de " + acteMariage.getProfessionEpoux() + ", " +
                            "fils de " + acteMariage.getNomparentepoux() + ", né(e) le " + acteMariage.getNaissanceparentepoux() +
                            " à " + acteMariage.getLieunaissanceparentepoux() + ", " +
                            "exerçant la profession de " + acteMariage.getProfessionparentepoux() +
                            " et domicilié à " + acteMariage.getDomicileparent() + ".";

// Ajouter le paragraphe complet au document
                    document.add(new Paragraph(paragraphContent));

// Ajouter un saut de ligne
                    document.add(new Paragraph("\n"));

// Informations sur l'ÉPOUSE
                    String paragraphContentEpouse = "L'épouse : " +
                            acteMariage.getPrenomEpouse() + " " + acteMariage.getNomEpouse() +
                            ", née le " + acteMariage.getNaissanceEpouse() + " à " + acteMariage.getLieunaissanceEpouse() + ", " +
                            "domiciliée à " + acteMariage.getDomicileEpouse() + ", en résidence à " + acteMariage.getResidenceEpouse() + ", " +
                            "exerçant la profession de " + acteMariage.getProfessionEpouse() + ", " +
                            "fille de " + acteMariage.getNomparentepouse() + ", né(e) le " + acteMariage.getNaissanceparentepouse() +
                            " à " + acteMariage.getLieunaissanceparentepouse() + ", " +
                            "exerçant la profession de " + acteMariage.getProfessionparentepouse() +
                            " et domiciliée à " + acteMariage.getDomicileparentepouse() + ".";

// Ajouter le paragraphe complet au document
                    document.add(new Paragraph(paragraphContentEpouse));

// Ajouter un saut de ligne
                    document.add(new Paragraph("\n"));

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
            // Récupérer l'ID de la mairie depuis la demande
            Integer mairieId = demande.getMairie().getId();

// Trouver le premier officier lié à la mairie

            Utilisateur officier = utilisateurRepository.findFirstByMairieId(mairieId)
                    .orElseThrow(() -> new RuntimeException("Aucun officier trouvé pour cette mairie"));

// Récupérer la signature de l'officier
            byte[] signatureBytes = utilisateurService.getSignature(officier.getId());

            Image signatureImage = Image.getInstance(signatureBytes);
            signatureImage.scaleToFit(250, 250); // Ajustez la taille de l'image si nécessaire
            signatureImage.setAlignment(Image.ALIGN_RIGHT);
            document.add(new Paragraph("\n"));
            document.add(signatureImage);
            Paragraph officierName = new Paragraph("Officier de l'État-Civil : " + officier.getNom() + " " + officier.getPrenom());
            officierName.setAlignment(Element.ALIGN_RIGHT);
            document.add(officierName);


           //-----------------------------------------------------------------------------
            PDFUtils.addQRCodeWithDetails(document);

            // Ajouter une image (signature) si nécessaire
            String signaturePath = "src/main/resources/static/signature.png";
            PDFUtils.addImageToPDF(document, signaturePath);




            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
