package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Citoyen;

import java.util.Optional;

@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Integer> {
    Optional<Citoyen> findByEmail(String email);
    Optional<Citoyen> findByToken(String token);

}
