package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("Citoyen")
public class Citoyen extends Utilisateur{


    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String num_tel;

    @Column(nullable = false)
    private String lieu_naissance;

    @Column(nullable = false)
    private Date date_naissance;

    @Column(nullable = false)
    private String cni;

    @OneToMany(mappedBy = "citoyen")
    private List<Demande> demande;
}
