package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.NaissanceDocument;

import java.util.Optional;

@Repository
public interface NaissanceDocumentRepository extends JpaRepository<NaissanceDocument,Integer> {
    NaissanceDocument findByDemande(Demande demande);

    boolean existsByNumeroRegistreAndAnNumeroAndPrenomAndNom(String numeroRegistre, String AnNumero, String prenom, String nom);
    Optional<NaissanceDocument> findByNumeroRegistreAndAnNumeroAndPrenomAndNom(String numeroRegistre, String AnNumero, String prenom, String nom);

    Optional<NaissanceDocument> findByNumeroRegistre(String numeroRegistre);
}
