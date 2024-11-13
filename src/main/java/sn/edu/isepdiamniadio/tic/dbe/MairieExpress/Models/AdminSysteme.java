package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("ADMINSYSTEM")
@Getter
@Setter
public class AdminSysteme extends Utilisateur{

    @ManyToMany
    private List<Mairie> mairieList;
}
