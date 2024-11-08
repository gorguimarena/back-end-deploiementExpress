package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestRegister {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private String role;
}
