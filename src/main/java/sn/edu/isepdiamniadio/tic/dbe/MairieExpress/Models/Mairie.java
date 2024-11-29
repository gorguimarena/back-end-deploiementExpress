package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class Mairie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;



    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String departement;

    @Column(nullable = false)
    private String commune;

    @Column(nullable = false)
    private String nom;

    @OneToMany(mappedBy = "mairie")
    private List<Document> documents;

    @ManyToMany
    @JsonIgnore
    private List<AdminSysteme> adminSystemes;

    @OneToMany(mappedBy = "mairie")
    @JsonIgnore
    private List<AdminMairie> adminMairie;

    @OneToMany(mappedBy = "mairie")
    @JsonIgnore
    private List<Officier> officiers;

    @OneToMany(mappedBy = "mairie")
    @JsonIgnore
    private List<Agent> agents;


    public Mairie() {} // Constructeur avec ID

    public Mairie(Integer id) {
        this.id = id;
    }

    public Mairie(String region, String departement, String commune, String nom) {
        this.region = region;
        this.departement = departement;
        this.commune = commune;
        this.nom = nom;
    }
}
