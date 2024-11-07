package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("AGENT")
@Getter
@Setter
public class Agent extends Utilisateur{

    @ManyToMany
    private List<Demande> demandes;

    @ManyToOne
    private Mairie mairie;
}
