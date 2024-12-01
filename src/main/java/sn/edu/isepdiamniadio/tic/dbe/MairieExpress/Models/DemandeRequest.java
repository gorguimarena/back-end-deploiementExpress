package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DemandeRequest {
    private Integer citoyenId;
    private Integer mairieId;
    private String typeDocument;
    private String numeroRegistre;
    private String annee;
    private String prenom;
    private String nom;
    private String AnNumero; // Nouvel attribut pour les documents de naissance
    private String numeroActeMariage;
    private String prenomEpoux;
    private String nomEpoux;
    private String prenomEpouse;
    private String nomEpouse;
    private LocalDate datemary; // Nouvel attribut pour les documents de mariage



}

