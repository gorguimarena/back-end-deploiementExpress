package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MairieRequest {

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("departement")
    private String departement;

    @JsonProperty("commune")
    private String commune;

    @JsonProperty("region")
    private String region;
}
