package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mairie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String departement;

    @Column(nullable = false)
    private String commune;

    @OneToMany(mappedBy = "mairie")
    private List<Document> documents;

    @ManyToMany
    private List<AdminSysteme> adminSystemes;

    @OneToOne
    private AdminMairie adminMairie;

    @OneToOne(mappedBy = "mairie")
    private Officier officier;


    @OneToMany(mappedBy = "mairie")
    private List<Citoyen> citoyens;

    @OneToMany(mappedBy = "mairie")
    private List<Agent> agents;

}
