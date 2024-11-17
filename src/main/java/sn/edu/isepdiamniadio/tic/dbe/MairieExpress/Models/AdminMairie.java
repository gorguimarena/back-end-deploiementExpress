package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@DiscriminatorValue("ADMINMAIRIE")
@Getter
@Setter
public class AdminMairie extends Utilisateur{

    @ManyToOne
    private Mairie mairie;

}
