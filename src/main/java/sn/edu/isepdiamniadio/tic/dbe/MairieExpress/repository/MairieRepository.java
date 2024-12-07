package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MairieRepository extends JpaRepository<Mairie, Integer> {
    Optional<Mairie> findById(Integer id);
    List<Mairie> findByNomContainingIgnoreCase(String query);
}
