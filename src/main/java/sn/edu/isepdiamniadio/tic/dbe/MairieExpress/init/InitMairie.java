package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;

@Component
@Order(2)
public class InitMairie implements CommandLineRunner {

    @Autowired
    private MairieRepository mairieRepository;

    @Override
    public void run(String... args) throws Exception {









    }
}
