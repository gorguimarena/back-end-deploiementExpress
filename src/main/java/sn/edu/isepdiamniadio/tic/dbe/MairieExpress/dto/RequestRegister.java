package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto;

import lombok.Getter;

@Getter
public class RequestRegister {
    private final String email;
    private final String password;
    private final String name;
    private final String phone;
    private final String address;
    private final String token;
    public RequestRegister(String email, String password, String name, String phone, String address, String token) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.token = token;
    }
}
