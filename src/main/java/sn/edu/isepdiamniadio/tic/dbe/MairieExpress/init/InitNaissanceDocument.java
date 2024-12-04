package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.NaissanceDocument;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.NaissanceDocumentRepository;

@Component
public class InitNaissanceDocument implements CommandLineRunner {
    @Autowired
    private NaissanceDocumentRepository naissanceDocumentRepository;

    @Autowired
    private MairieRepository mairieRepository;


    @Override
    public void run(String... args) throws Exception {



//---------------------------------------------------------------------------------------------------
        // Récupérer une mairie existante (par exemple, la mairie avec ID 1, mais en Integer)
        Mairie mairiea = mairieRepository.findById(1)  // ID Integer
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));

        // Création d'un NaissanceDocument
        NaissanceDocument naissanceDoc1 = new NaissanceDocument();
        naissanceDoc1.setAnNumero("2001");
        naissanceDoc1.setAnLettre("Deux mille un");
        naissanceDoc1.setNumeroRegistre("937");
        naissanceDoc1.setNumRegistreLettre("Neuf cent trente sept");
        naissanceDoc1.setSexe("Masculin");
        naissanceDoc1.setDateNaisanceLettre("vingt deux février deux mille un");
        naissanceDoc1.setHeureNaissancelettre("Vingt-deux heures");
        naissanceDoc1.setLieuNaissance("Dakar");
        naissanceDoc1.setPrenom("Boubou Djamil");
        naissanceDoc1.setNom("Diop");
        naissanceDoc1.setPrenomPere("Oumar");
        naissanceDoc1.setPrenomMere("Coumba");
        naissanceDoc1.setNomMere("Ba");

        // Associer la mairie au document
        naissanceDoc1.setMairie(mairiea);

        // Enregistrer le document dans la base de données
        naissanceDocumentRepository.save(naissanceDoc1);
//------------------------------------------------------------------------------------------


        Mairie mairie1 = mairieRepository.findById(1)  // ID Integer
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));
        // Création d'un NaissanceDocument
        NaissanceDocument naissanceDoc2 = new NaissanceDocument();
        naissanceDoc2.setAnNumero("200O");
        naissanceDoc2.setAnLettre("Deux mille");
        naissanceDoc2.setNumeroRegistre("37");
        naissanceDoc2.setNumRegistreLettre("Neuf cent trente sept");
        naissanceDoc2.setDateNaisanceLettre("vingt deux Novembre deux mille ");
        naissanceDoc2.setSexe("Masculin");
        naissanceDoc2.setHeureNaissancelettre("Vingt-deux heures");
        naissanceDoc2.setLieuNaissance("Dakar");
        naissanceDoc2.setPrenom("Gorgui");
        naissanceDoc2.setNom("Diop");
        naissanceDoc2.setPrenomPere("Djamil");
        naissanceDoc2.setPrenomMere("Soumayatu");
        naissanceDoc2.setNomMere("Diena");

        // Associer la mairie au document
        naissanceDoc2.setMairie(mairie1);

        // Enregistrer le document dans la base de données
        naissanceDocumentRepository.save(naissanceDoc2);

//-------------------------------------------------------------------------------

        System.out.println("Document de naissance inséré : " + naissanceDoc1);
    }
}
