package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestAuth {
    @JsonProperty("email")
    String username;

    @JsonProperty("password")
    String password;
}
