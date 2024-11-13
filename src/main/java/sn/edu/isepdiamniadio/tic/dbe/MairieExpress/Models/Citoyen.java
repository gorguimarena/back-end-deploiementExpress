package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;


import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("CITOYEN")
public class Citoyen extends Utilisateur{

    @OneToMany(mappedBy = "citoyen")
    private List<Demande> demandes;

    @OneToMany
    private List<RendezVous> rendezVous;

    @ManyToOne
    private Mairie mairie;
}
