package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import lombok.Getter;

@Getter
public class RequestRegister {
    private final String email;
    private final String password;
    private final String nom;
    private final String prenom;
    private final String username;
    private final String role;
    public RequestRegister(String email, String password, String nom, String prenom, String username,String role) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.role = role;
    }
}
