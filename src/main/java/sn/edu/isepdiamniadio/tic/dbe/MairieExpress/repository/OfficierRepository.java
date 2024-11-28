package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Officier;

import java.util.Optional;

@Repository
public interface OfficierRepository extends JpaRepository<Officier,Integer> {
    Optional<Officier> findByMairie(Mairie mairie);
}
