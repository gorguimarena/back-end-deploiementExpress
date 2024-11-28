package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.MariageDocument;

@Repository
public interface MariageDocumentRepository extends JpaRepository<MariageDocument,Long> {
    MariageDocument findByDemande(Demande demande);
}
