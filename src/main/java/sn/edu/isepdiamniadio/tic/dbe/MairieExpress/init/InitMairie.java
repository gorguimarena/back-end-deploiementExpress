package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;

@Component
public class InitMairie implements CommandLineRunner {

    @Autowired
    private MairieRepository mairieRepository;

    @Override
    public void run(String... args) throws Exception {

        Mairie mairie = Mairie.builder()
                .region("Dakar")
                .nom("Centre de l'etat civil Dakar plateau")
                .commune("Dakar plateau")
                .departement("Dakar")
                .build();

        mairieRepository.save(mairie);






    }
}
