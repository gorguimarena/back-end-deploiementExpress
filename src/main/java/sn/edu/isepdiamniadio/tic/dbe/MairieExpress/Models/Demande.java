package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private Date date_demande;


    private String status_demande;

    @ManyToOne
    private Citoyen citoyen;

    @ManyToMany
    private List<Document> document;

    @ManyToMany
    private List<Agent> agents;

}
