package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("CITOYEN")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Citoyen extends Utilisateur{

    @OneToMany(mappedBy = "citoyen")
    private List<Demande> demandes;

    @OneToMany
    private List<RendezVous> rendezVous;

}
