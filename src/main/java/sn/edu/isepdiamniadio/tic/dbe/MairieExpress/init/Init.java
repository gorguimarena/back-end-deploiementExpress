package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Role;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Mairie Express");

        Role roleCitoyen = Role.builder()
                .code("citoyen")
                .nom("citoyen")
                .build();

        Role roleOfficier = Role.builder()
                .code("officier")
                .nom("officier")
                .build();

        Role roleAdminSyst = Role.builder()
                .code("adminSysteme")
                .nom("adminSysteme")
                .build();

        Role roleAdminMairie = Role.builder()
                .code("adminMairie")
                .nom("adminMairie")
                .build();

        Role roleAgent = Role.builder()
                .code("agent")
                .nom("agent")
                .build();

        Role []roles = {
                roleCitoyen,
                roleOfficier,
                roleAdminSyst,
                roleAdminMairie,
                roleAgent
        };

        roleRepository.saveAll(Arrays.asList(roles));

    }
}
