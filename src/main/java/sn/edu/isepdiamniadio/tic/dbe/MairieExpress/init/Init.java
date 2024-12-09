
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
    CitoyenRepository citoyenRepository;



    @Override
    public void run(String... args) throws Exception {
        System.out.println("Mairie Express");

        Role roleCitoyen = Role.builder()
                .code("citoyen")
                .nom("citoyen")
                .build();

        Role roleAdminSyst = Role.builder()
                .code("adminSystem")
                .nom("adminSystem")
                .build();



        Role roleB = roleRepository.save(roleCitoyen);

        Role roleC = roleRepository.save(roleAdminSyst);

        AdminSysteme adminSysteme = new AdminSysteme();

        adminSysteme.setRoles(Collections.singletonList(roleC));
        adminSysteme.setEmail("adminsystem@gmail.com");
        adminSysteme.setNom("Admin");
        adminSysteme.setPrenom("System");
        adminSystemeRepository.save(adminSysteme);



        Citoyen citoyen = new Citoyen();

        citoyen.setRoles(Collections.singletonList(roleB));
        citoyen.setEmail("citoyen@gmail.com");
        citoyen.setNom("Citoyen");
        citoyen.setPrenom("Citoyen");

        citoyenRepository.save(citoyen);

    }
}
