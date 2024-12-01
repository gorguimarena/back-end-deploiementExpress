package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;





import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.MariageDocument;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.NaissanceDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generatePdf(Demande demande, Object documentInfos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Mairie: " + demande.getMairie().getNom()));



            switch (demande.getTypeDocument()) {
                case "extrait_de_naissance":
                    NaissanceDocument extraitNaissance = (NaissanceDocument) documentInfos;
                    document.add(new Paragraph("EXTRAIT DU REGISTRE DES ACTES DE NAISSANCE"));
                    document.add(new Paragraph("Pour l'année : " + extraitNaissance.getAnLettre()));
                    document.add(new Paragraph("Numéro : " + extraitNaissance.getNumRegistreLettre()));
                    document.add(new Paragraph("An : " + extraitNaissance.getAnNumero()));
                    document.add(new Paragraph(extraitNaissance.getNumeroRegistre() + "N dans les registres en chiffres"));
                    document.add(new Paragraph("Prénom : " + extraitNaissance.getPrenom()));
                    document.add(new Paragraph("Nom : " + extraitNaissance.getNom()));
                    document.add(new Paragraph("Sexe : " + extraitNaissance.getSexe()));
                    document.add(new Paragraph("Lieu de naissance : " + extraitNaissance.getLieuNaissance()));
                    document.add(new Paragraph("Date de naissance : " + extraitNaissance.getDateNaisanceLettre()));
                    document.add(new Paragraph("Heure de naissance : " + extraitNaissance.getHeureNaissancelettre()));
                    document.add(new Paragraph("Nom du père : " + extraitNaissance.getPrenomPere() + " " + extraitNaissance.getNomPere()));
                    document.add(new Paragraph("Nom de la mère : " + extraitNaissance.getPrenomMere() + " " + extraitNaissance.getNomMere()));
                    if (extraitNaissance.getNumJugement() != null) {
                        document.add(new Paragraph("Numéro de jugement : " + extraitNaissance.getNumJugement()));
                    }
                    break;

                case "copie_litterale_d_acte_de_naissance":
                    NaissanceDocument copieLitterale = (NaissanceDocument) documentInfos;
                    document.add(new Paragraph("COPIE LITTÉRALE DE L'ACTE DE NAISSANCE"));
                    document.add(new Paragraph("Pour l'année : " + copieLitterale.getAnLettre()));
                    document.add(new Paragraph("Numéro : " + copieLitterale.getNumRegistreLettre()));
                    document.add(new Paragraph("Année : " + copieLitterale.getAnNumero()));
                    document.add(new Paragraph("Prénom : " + copieLitterale.getPrenom()));
                    document.add(new Paragraph("Nom : " + copieLitterale.getNom()));
                    document.add(new Paragraph("Sexe : " + copieLitterale.getSexe()));
                    document.add(new Paragraph("Lieu de naissance : " + copieLitterale.getLieuNaissance()));
                    document.add(new Paragraph("Date de naissance : " + copieLitterale.getDateNaisanceLettre()));
                    document.add(new Paragraph("Heure de naissance : " + copieLitterale.getHeureNaissancelettre()));
                    document.add(new Paragraph("Nom du père : " + copieLitterale.getPrenomPere() + " " + copieLitterale.getNomPere()));
                    document.add(new Paragraph("Nom de la mère : " + copieLitterale.getPrenomMere() + " " + copieLitterale.getNomMere()));
                    break;

                case "certificat_de_mariage_constante":
                    MariageDocument copieIntegraleMariage = (MariageDocument) documentInfos;
                    document.add(new Paragraph("COPIE INTÉGRALE DE L'ACTE DE MARIAGE"));
                    document.add(new Paragraph("Numéro d'acte : " + copieIntegraleMariage.getNumeroActeMariage()));
                    document.add(new Paragraph("Date de mariage : " + copieIntegraleMariage.getDate()));
                    document.add(new Paragraph("Date en lettres : " + copieIntegraleMariage.getDatemary()));
                    document.add(new Paragraph("Régime matrimonial : " + copieIntegraleMariage.getRegimeMatrimonial()));
                    document.add(new Paragraph("Dot : " + (copieIntegraleMariage.getDote() != null ? copieIntegraleMariage.getDote() + " FCFA" : "Non spécifiée")));
                    document.add(new Paragraph("Option : " + copieIntegraleMariage.getOpter()));
                    document.add(new Paragraph(""));

                    // Ajouter les mêmes informations détaillées que pour "acte_de_mariage".
                    document.add(new Paragraph("Les informations complètes sont identiques à l'acte de mariage."));
                    break;

                case "copie_litterale_acte_de_mariage":
                    MariageDocument acteMariage = (MariageDocument) documentInfos;
                    document.add(new Paragraph("ACTE DE MARIAGE"));
                    document.add(new Paragraph("Numéro d'acte : " + acteMariage.getNumeroActeMariage()));
                    document.add(new Paragraph("Date de mariage : " + acteMariage.getDate()));
                    document.add(new Paragraph("Date en lettres : " + acteMariage.getDatemary()));
                    document.add(new Paragraph("Régime matrimonial : " + acteMariage.getRegimeMatrimonial()));
                    document.add(new Paragraph("Dot : " + (acteMariage.getDote() != null ? acteMariage.getDote() + " FCFA" : "Non spécifiée")));
                    document.add(new Paragraph("Option : " + acteMariage.getOpter()));
                    document.add(new Paragraph(""));

                    // Informations sur l'époux
                    document.add(new Paragraph("INFORMATIONS SUR L'ÉPOUX :"));
                    document.add(new Paragraph("Prénom : " + acteMariage.getPrenomEpoux()));
                    document.add(new Paragraph("Nom : " + acteMariage.getNomEpoux()));
                    document.add(new Paragraph("Naissance : " + acteMariage.getNaissanceEpoux()));
                    document.add(new Paragraph("Lieu de naissance : " + acteMariage.getLieunaissanceEpoux()));
                    document.add(new Paragraph("Domicile : " + acteMariage.getDomicileEpoux()));
                    document.add(new Paragraph("Résidence : " + acteMariage.getResidenceEpoux()));
                    document.add(new Paragraph("Profession : " + acteMariage.getProfessionEpoux()));
                    document.add(new Paragraph("Nom du parent : " + acteMariage.getNomparentepoux()));
                    document.add(new Paragraph("Naissance du parent : " + acteMariage.getNaissanceparentepoux()));
                    document.add(new Paragraph("Lieu de naissance du parent : " + acteMariage.getLieunaissanceparentepoux()));
                    document.add(new Paragraph("Profession du parent : " + acteMariage.getProfessionparentepoux()));
                    document.add(new Paragraph("Domicile du parent : " + acteMariage.getDomicileparent()));
                    document.add(new Paragraph(""));

                    // Informations sur l'épouse
                    document.add(new Paragraph("INFORMATIONS SUR L'ÉPOUSE :"));
                    document.add(new Paragraph("Prénom : " + acteMariage.getPrenomEpouse()));
                    document.add(new Paragraph("Nom : " + acteMariage.getNomEpouse()));
                    document.add(new Paragraph("Naissance : " + acteMariage.getNaissanceEpouse()));
                    document.add(new Paragraph("Lieu de naissance : " + acteMariage.getLieunaissanceEpouse()));
                    document.add(new Paragraph("Domicile : " + acteMariage.getDomicileEpouse()));
                    document.add(new Paragraph("Résidence : " + acteMariage.getResidenceEpouse()));
                    document.add(new Paragraph("Profession : " + acteMariage.getProfessionEpouse()));
                    document.add(new Paragraph("Nom du parent : " + acteMariage.getNomparentepouse()));
                    document.add(new Paragraph("Naissance du parent : " + acteMariage.getNaissanceparentepouse()));
                    document.add(new Paragraph("Lieu de naissance du parent : " + acteMariage.getLieunaissanceparentepouse()));
                    document.add(new Paragraph("Profession du parent : " + acteMariage.getProfessionparentepouse()));
                    document.add(new Paragraph("Domicile du parent : " + acteMariage.getDomicileparentepouse()));
                    document.add(new Paragraph(""));

                    // Témoins
                    document.add(new Paragraph("TÉMOINS :"));
                    document.add(new Paragraph("Premier témoin : " + acteMariage.getPremiertemoin()));
                    document.add(new Paragraph("Deuxième témoin : " + acteMariage.getDeuxiemetemoin()));
                    break;

                default:
                    throw new IllegalArgumentException("Type de document inconnu: " + demande.getTypeDocument());
            }

            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
