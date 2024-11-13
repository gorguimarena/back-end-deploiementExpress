package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@DiscriminatorValue("ADMINMAIRIE")
@Getter
@Setter
public class AdminMairie extends Utilisateur{

    @OneToOne(mappedBy = "adminMairie")
    private Mairie mairie;

}
