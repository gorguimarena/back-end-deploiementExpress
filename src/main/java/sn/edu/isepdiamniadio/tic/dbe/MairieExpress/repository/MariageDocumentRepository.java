package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.MariageDocument;

import java.util.Optional;

@Repository
public interface MariageDocumentRepository extends JpaRepository<MariageDocument,Long> {
    MariageDocument findByDemande(Demande demande);


    Optional<MariageDocument> findByNumeroActeMariageAndNomEpouxAndPrenomEpouxAndNomEpouseAndPrenomEpouse(String numeroActeMariage, String nomEpoux, String prenomEpoux, String nomEpouse, String prenomEpouse);

    boolean existsByNumeroActeMariageAndPrenomEpouxAndNomEpouxAndPrenomEpouseAndNomEpouse( String numeroActeMariage, String prenomEpoux, String nomEpoux, String prenomEpouse, String nomEpouse);

    Object findByNumeroActeMariageAndPrenomEpouxAndNomEpouxAndPrenomEpouseAndNomEpouse(String numeroActeMariage, String prenomepoux, String nomepoux, String prenomepouse, String nomepouse);
}
