package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;

import java.util.Collections;

@Component
@Order(2)
public class InitMairie implements CommandLineRunner {

    @Autowired
    private MairieRepository mairieRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AdminMairieRepository adminMairieRepository;

    @Autowired
    private OfficierRepository officierRepository;

    @Override
    public void run(String... args) throws Exception {


        Mairie mairie = Mairie.builder()
                .region("Dakar")
                .nom("Centre de l'etat civil Dakar plateau")
                .commune("Dakar plateau")
                .departement("Dakar")
                .build();

        Mairie mairie1 = mairieRepository.save(mairie);

        Role roleAdminMairie = Role.builder()
                .code("adminMairie")
                .nom("adminMairie")
                .build();

        Role roleOfficier = Role.builder()
                .code("officier")
                .nom("officier")
                .build();

        Role roleAgent = Role.builder()
                .code("agent")
                .nom("agent")
                .build();

        Role roleAgentSave = roleRepository.save(roleAgent);
        Role roleAdminMairieSave = roleRepository.save(roleAdminMairie);
        Role roleOfficierSave = roleRepository.save(roleOfficier);

        Agent agent = new Agent();
        agent.setMairie(mairie1);
        agent.setEmail("agent@gmail.com");
        agent.setNom("Agent");
        agent.setPrenom("AgentMairie");
        agent.setRoles(Collections.singletonList(roleAgentSave));



        AdminMairie adminMairie = new AdminMairie();
        adminMairie.setRoles(Collections.singletonList(roleAdminMairieSave));
        adminMairie.setEmail("adminmairie@gmail.com");
        adminMairie.setNom("Admin");
        adminMairie.setMairie(mairie1);
        adminMairie.setPrenom("Mairie");

        Officier officier = new Officier();
        officier.setMairie(mairie1);
        officier.setNom("Officier");
        officier.setRoles(Collections.singletonList(roleOfficierSave));
        officier.setEmail("officier@gmail.com");
        officier.setPrenom("OfficierMairie");

        adminMairieRepository.save(adminMairie);
        agentRepository.save(agent);
        officierRepository.save(officier);

    }
}
