package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;

import java.util.List;

@Repository
public interface DocumentEnvoyeRepository extends JpaRepository<DocumentEnvoye,Long> {
    List<DocumentEnvoye> findByCitoyenId(Integer citoyenId);
}
