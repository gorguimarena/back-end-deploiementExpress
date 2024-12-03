package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.AdminSystemeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.CitoyenRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Order(1)
public class Init implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    AdminSystemeRepository adminSystemeRepository;

    @Autowired
    private MairieRepository mairieRepository;

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
                .code("adminSystem")
                .nom("adminSystem")
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
                roleOfficier,
                roleCitoyen,
                roleAgent
        };

        roleRepository.saveAll(Arrays.asList(roles));
        Role roleC = roleRepository.save(roleAdminSyst);
        Role roleA = roleRepository.save(roleAdminMairie);

        AdminSysteme adminSysteme = new AdminSysteme();

        adminSysteme.setRoles(Collections.singletonList(roleA));
        adminSysteme.setEmail("adminsystem@gmail.com");
        adminSysteme.setNom("Admin");
        adminSysteme.setPrenom("System");

        AdminMairie adminMairie = new AdminMairie();
        adminMairie.setRoles(Collections.singletonList(roleC));
        adminMairie.setEmail("adminmairie@gmail.com");
        adminMairie.setNom("Admin");
        adminMairie.setPrenom("Mairie");
        adminSystemeRepository.save(adminSysteme);
    }
}
