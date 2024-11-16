package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_user")
@NoArgsConstructor
@Getter
@Setter
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Integer id;

    protected String nom;

    protected String prenom;

    @Column(nullable = false)
    protected String email;

    @ManyToMany
    protected List<Role> roles;
}
