package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.MariageDocument;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MariageDocumentRepository;

import java.time.LocalDate;
import java.util.Date;

@Component
public class InitMariageDocument implements CommandLineRunner {

    @Autowired
    private MariageDocumentRepository mariageDocumentRepository;

    @Autowired
    private MairieRepository mairieRepository;


    @Override
    public void run(String... args) throws Exception {
        Mairie mairie0 = Mairie.builder()
                .region("Dakar")
                .nom("Centre de l'etat civil")
                .commune("Sam notaire")
                .departement("Dakar")
                .build();

        mairieRepository.save(mairie0);


        Mairie mairie = mairieRepository.findById(1)  // ID Integer
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));

        // Créer une instance de MariageDocument
        MariageDocument mariageDoc1 = new MariageDocument();
        mariageDoc1.setNumeroActeMariage("299");
        mariageDoc1.setDate(new Date());  // Date actuelle
        mariageDoc1.setDatemary(LocalDate.of(2010, 2, 15));  // Exemple de date

        // Informations sur l'époux
        mariageDoc1.setPrenomEpoux("Gorgui");
        mariageDoc1.setNomEpoux("Marena");
        mariageDoc1.setNaissanceEpoux("2000-01-15");
        mariageDoc1.setLieunaissanceEpoux("Dakar");
        mariageDoc1.setDomicileEpoux("Khar Yalla");
        mariageDoc1.setResidenceEpoux("Diamniadio");
        mariageDoc1.setProfessionEpoux("Ingénieur");
        mariageDoc1.setNomparentepoux("Djamil Diop");
        mariageDoc1.setNaissanceparentepoux("1960-05-20");
        mariageDoc1.setLieunaissanceparentepoux("Thies");
        mariageDoc1.setProfessionparentepoux("Médecin");
        mariageDoc1.setDomicileparent("TIvaouane");


        // Informations sur l'épouse
        mariageDoc1.setPrenomEpouse("Anna Soumayatou");
        mariageDoc1.setNomEpouse(" Diena");
        mariageDoc1.setNaissanceEpouse("2000-11-30");
        mariageDoc1.setLieunaissanceEpouse("Mbour");
        mariageDoc1.setDomicileEpouse("Mbour");
        mariageDoc1.setResidenceEpouse("Diamniadio");
        mariageDoc1.setProfessionEpouse("DEveloppeuse");
        mariageDoc1.setNomparentepouse("Abdoulaye Mbaye");
        mariageDoc1.setNaissanceparentepouse("1999-08-25");
        mariageDoc1.setLieunaissanceparentepouse("Ouakam");
        mariageDoc1.setProfessionparentepouse("Professeur");
        mariageDoc1.setDomicileparentepouse("Dakar");


        // Autres informations
        mariageDoc1.setOpter("Opté pour mariage civil");
        mariageDoc1.setDote(2500);
        mariageDoc1.setRegimeMatrimonial("Séparation de biens");

        // Témoins
        mariageDoc1.setPremiertemoin("Boubou Djamil Diop");
        mariageDoc1.setDeuxiemetemoin("Abdoulaye Ly");

        // Associer la mairie au document
        mariageDoc1.setMairie(mairie);

        // Enregistrer le document de mariage
        mariageDocumentRepository.save(mariageDoc1);

        System.out.println("MariageDocument inséré avec succès !");

    }
}
