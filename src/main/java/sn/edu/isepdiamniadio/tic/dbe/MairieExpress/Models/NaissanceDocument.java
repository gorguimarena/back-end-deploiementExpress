package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class NaissanceDocument extends Document {

    @Column(nullable = false)
    private String AnNumero;

    @Column(nullable = false)
    private String AnLettre;

    @Column(nullable = false)
    private String numeroRegistre;

    @Column(nullable = false)
    private String numRegistreLettre;

    @Column(nullable = false)
    private String dateNaisanceLettre;

    @Column(nullable = false)
    private String heureNaissancelettre;

    @Column(nullable = false)
    private String lieuNaissance;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenomPere;

    @Column(nullable = false)
    private String nomPere;

    @Column(nullable = false)
    private String prenomMere;

    @Column(nullable = false)
    private String nomMere;


    @Column(nullable = true)
    private String numJugement;

    @ManyToOne
    private Demande demande;



}


