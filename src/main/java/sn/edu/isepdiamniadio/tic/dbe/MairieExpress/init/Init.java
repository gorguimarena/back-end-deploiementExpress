
package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.init;

import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;

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
    AdminMairieRepository adminMairieRepository;

    @Autowired
    private MairieRepository mairieRepository;

    @Autowired
    private OfficierRepository officierRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Mairie Express");

        Mairie mairie = Mairie.builder()
                .region("Dakar")
                .nom("Centre de l'etat civil Dakar plateau")
                .commune("Dakar plateau")
                .departement("Dakar")
                .build();

        mairieRepository.save(mairie);

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
                roleCitoyen,
                roleAgent
        };

        roleRepository.saveAll(Arrays.asList(roles));
        Role roleC = roleRepository.save(roleAdminSyst);
        Role roleA = roleRepository.save(roleAdminMairie);
        Role off=roleRepository.save(roleOfficier);

        Mairie mairieg = mairieRepository.findById(1) .orElseThrow(() -> new RuntimeException("Mairie not found"));

        Officier officier=new Officier();
        officier.setRoles(Collections.singletonList(off));
        officier.setEmail("hello@gmail.com");
        officier.setNom("bubu");
        officier.setPrenom("bubu");
        officier.setMairie(mairieg);
        officierRepository.save(officier);


        AdminSysteme adminSysteme = new AdminSysteme();

        adminSysteme.setRoles(Collections.singletonList(roleC));
        adminSysteme.setEmail("adminsystem@gmail.com");
        adminSysteme.setNom("Admin");
        adminSysteme.setPrenom("System");

        AdminMairie adminMairie = new AdminMairie();
        adminMairie.setRoles(Collections.singletonList(roleA));
        adminMairie.setEmail("adminmairie@gmail.com");
        adminMairie.setNom("Admin");
        adminMairie.setPrenom("Mairie");
        adminSystemeRepository.save(adminSysteme);
        adminMairieRepository.save(adminMairie);

    }
}
