package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.NaissanceDocument;

@Repository
public interface NaissanceDocumentRepository extends JpaRepository<NaissanceDocument,Integer> {
    NaissanceDocument findByDemande(Demande demande);

}
