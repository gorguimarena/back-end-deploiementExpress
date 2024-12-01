package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Attributs spécifiques pour demande Extrait du registre des actes de naissance ou copie littérale
    private String numeroRegistre;
    private String prenomInteresse;   // Prénom de l'individu concerné par le document
    private String nomInteresse;
    private String AnNumero;

    // Attributs spécifiques pour demande de certificat d'acte de Mariage ou copie intégrale
    private String numeroActeMariage;
    private String prenomepoux;
    private String nomepoux;
    private String prenomepouse;
    private String nomepouse;
    private LocalDate datemary;

    //Attributs communs à toute demande
    private Date dateDemande;
    private String statutDemande;
    private String typeDocument;


    @ManyToOne
    private Citoyen citoyen;

    @ManyToOne
    private Document document;

    @ManyToOne
    private Mairie mairie;

    private String pdfUrl;


    public boolean isNaissanceDocument() {
        return "extrait_de_naissance".equals(typeDocument) || "copie_litterale_d_acte_de_naissance".equals(typeDocument);
    }


    public boolean isMariageDocument() {
        return "certificat_de_mariage_constante".equals(typeDocument) || "copie_litterale_acte_de_mariage".equals(typeDocument);
    }
}
