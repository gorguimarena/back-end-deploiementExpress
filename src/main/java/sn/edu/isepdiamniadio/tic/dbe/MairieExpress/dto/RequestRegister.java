package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestRegister {

    @JsonProperty("email")
    private String email;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("role")
    private String role;

    @JsonProperty("idMairie")
    private Integer idMirie;
}
