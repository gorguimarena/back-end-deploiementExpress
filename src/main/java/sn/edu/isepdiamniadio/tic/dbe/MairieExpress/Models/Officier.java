package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("OFFICIER")
public class Officier extends Utilisateur{

    @ManyToOne
    @JsonIgnore
    private Mairie mairie;

    @Lob
    private byte[] signature;


}
