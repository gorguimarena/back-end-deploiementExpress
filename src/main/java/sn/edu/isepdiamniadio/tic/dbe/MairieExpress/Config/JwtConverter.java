package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Value("${jwt.auth.converter.resource-id}")
    private String clientId ;

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> resourceAccess = source.getClaim("resource_access");

        if (resourceAccess == null || resourceAccess.isEmpty()) {
            return new  ArrayList<>();
        }
        Map<String,Object> roles = (Map<String, Object>) resourceAccess.get(clientId);

        Collection<GrantedAuthority> authorities = ((List<String>) roles.get("roles"))
                .stream().map(roleName -> "ROLE_"+roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }
}
