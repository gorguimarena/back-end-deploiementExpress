package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("OFFICIER")
public class Officier extends Utilisateur{

    @ManyToOne
    private Mairie mairie;

    @Lob
    private byte[] signature;


}
