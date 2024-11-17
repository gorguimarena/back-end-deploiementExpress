package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role{
    @Id
    @Column(name = "code", nullable = false)
    private String code;

    private String nom;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<Utilisateur> utilisateur;
}
