package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;

import java.util.List;
import java.util.Optional;


public interface DemandeRepository extends JpaRepository<Demande,Long> {
    List<Demande> findByCitoyen(Citoyen citoyen);
    Optional<Demande> findByCitoyenId(Citoyen citoyen);

    List<Demande> findByMairie_IdAndStatutDemande(Long mairieId, String enAttente);

    List<Demande> findByMairieId(Integer mairieId);
}
