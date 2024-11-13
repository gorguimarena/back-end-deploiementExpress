package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String motif;

    @ManyToOne
    private Citoyen citoyen;
}
