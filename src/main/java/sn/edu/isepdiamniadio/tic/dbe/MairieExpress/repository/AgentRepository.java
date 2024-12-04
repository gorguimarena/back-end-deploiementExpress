package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Agent;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {
    Optional<Agent> findByMairie(Mairie mairie);
}
