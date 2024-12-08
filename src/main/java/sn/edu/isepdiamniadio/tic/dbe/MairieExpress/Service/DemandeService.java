package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.DemandeRequest;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
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

    @Autowired
    private NotificationRepository notificationRepository;

    public boolean checkDocumentExists(DemandeRequest demandeRequest) {
        if ("extrait_de_naissance".equals(demandeRequest.getTypeDocument()) || "copie_litterale_d_acte_de_naissance".equals(demandeRequest.getTypeDocument())) {
            Optional<NaissanceDocument> naissanceDocument = naissanceDocumentRepository.findByNumeroRegistre(demandeRequest.getNumeroRegistre());

            return
                    naissanceDocument.isPresent() &&
                            naissanceDocument.get().getNumeroRegistre().equals(demandeRequest.getNumeroRegistre()) &&
                            naissanceDocument.get().getPrenom().equalsIgnoreCase(demandeRequest.getPrenom()) &&
                            naissanceDocument.get().getNom().equalsIgnoreCase(demandeRequest.getNom());
        } else if ("certificat_de_mariage_constante".equals(demandeRequest.getTypeDocument()) || "copie_litterale_acte_de_mariage".equals(demandeRequest.getTypeDocument())) {
            return mariageDocumentRepository.existsByNumeroActeMariageAndPrenomEpouxAndNomEpouxAndPrenomEpouseAndNomEpouse(
                    demandeRequest.getNumeroActeMariage(), demandeRequest.getPrenomEpoux(), demandeRequest.getNomEpoux(), demandeRequest.getPrenomEpouse(), demandeRequest.getNomEpouse());
        }
        return false;
    }
    public List<Notification> getNotificationsByCitoyen(Integer citoyenId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new EntityNotFoundException("Citoyen non trouvé"));

        List<Notification> notifications = notificationRepository.findByCitoyenOrderByDateCreationDesc(citoyen);

        if (notifications.isEmpty()) {
            System.out.println("Aucune notification trouvée pour le citoyen ID : " + citoyenId);
        }

        return notifications;
    }

    public void markNotificationAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification non trouvée"));

        if (!"lu".equals(notification.getStatut())) {
            notification.setStatut("lu");
            notificationRepository.save(notification);
        } else {
            System.out.println("Notification ID : " + notificationId + " est déjà marquée comme lue.");
        }
    }

   /* public void createDemande(DemandeRequest demandeRequest, String token) {
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






    public void createDemande(DemandeRequest demandeRequest, String token) {
        try {
            String publicKeyPEM = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhCcGHVzOrwDbYkR5R3nhM/uH9ROgKKa6zvU0UQR9c/ThpPKTutPIwyxJ3sYBKqr8akWtO830n8ItTr6cskPocmq4G/y6C4LJPWFQ65nViVRGI4VveiLUHhB5UCrUEolnml6mrMXfuUD3YE7rvbbh0syREnjC+JfUNqd56mIaIyebRCDQ/wWH387+fvVZ2YzCwIEkpKzfrdIXiEalXvF/K/trZi885XVWVptpfBsr3seM1lmxuYYtzP94urwS14X4jbTeIE+TXR85+YSgeuIAuzKsmTjPthslKcvwMwXa5Bw7TmvD6FEfBYorFk1YjwRDK15clYm0rAf2N/7jp8puQwIDAQAB";

            // Décoder la clé publique
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Décoder le token JWT pour extraire les informations
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.get("email", String.class);  // Extraire l'adresse e-mail

            Optional<Citoyen> citoyenOpt = citoyenRepository.findByEmail(email);
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
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du décodage du token JWT", e);
        }
    }

*/


        public Demande createDemande(DemandeRequest demandeRequest, Citoyen citoyen) {
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

                return demandeRepository.save(demande);
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
