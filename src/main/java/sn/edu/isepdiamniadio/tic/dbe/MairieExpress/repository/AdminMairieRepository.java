package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.AdminMairie;

@Repository
public interface AdminMairieRepository extends JpaRepository<AdminMairie, Integer> {
}
