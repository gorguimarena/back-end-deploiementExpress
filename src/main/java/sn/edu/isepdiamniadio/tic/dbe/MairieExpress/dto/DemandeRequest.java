package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DemandeRequest {
    @JsonProperty("citoyenId")
    private Integer citoyenId;

    @JsonProperty("idMairie")
    private Integer mairieId;

    @JsonProperty("typeDocument")
    private String typeDocument;

    @JsonProperty("numeroRegistre")
    private String numeroRegistre;

    @JsonProperty("annee")
    private String annee;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("AnNumero")
    private String AnNumero;

    @JsonProperty("numeroActeMariage")
    private String numeroActeMariage;

    @JsonProperty("prenomEpoux")
    private String prenomEpoux;

    @JsonProperty("nomEpoux")
    private String nomEpoux;

    @JsonProperty("prenomEpouse")
    private String prenomEpouse;

    @JsonProperty("nomEpouse")
    private String nomEpouse;

    @JsonProperty("datemary")
    private LocalDate datemary;

}

