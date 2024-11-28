package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEnvoye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type du document envoyé (extrait de naissance, acte de mariage, etc.)
    private String typeDocument;

    // Chemin vers le fichier PDF généré
    private String pathDocument;

    // Date d'envoi du document
    private Date dateEnvoi;

    // Référence vers la demande associée
    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "citoyen_id")
    private Citoyen citoyen;


}
