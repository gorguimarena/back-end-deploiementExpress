package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {
    private String token;
    private String username;
    private String nom;
    private String prenom;
}
