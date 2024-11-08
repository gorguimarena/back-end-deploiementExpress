package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    public static final String CITOYEN = "citoyen";
    private static final String OFFICIER = "officier";
    private static final String ADMINSYSTEME = "adminSystem";
    private static final String ADMINMAIRIE = "adminMairie";
    private static final String AGENT = "agent";

    private final JwtConverter jwtConverter;

    // Configuration principale de Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtConverter);
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowCredentials(true);
                                config.setAllowedOrigins(Collections.singletonList("*"));
                                config.setAllowedHeaders(Collections.singletonList("*"));
                                config.setAllowedMethods(Collections.singletonList("*"));
                                config.setExposedHeaders(Collections.singletonList("Authorization"));
                                config.setMaxAge(3600L);
                                return config;
                            }
                        }));

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST,"/api/auth").permitAll()
                        .requestMatchers("/api/logout").permitAll()
                        .requestMatchers("/api/adminsystem/**").hasRole(ADMINSYSTEME)
                        .requestMatchers("/api/adminmairie/**").hasRole(ADMINMAIRIE)
                        .requestMatchers("/api/agent/**").hasRole(AGENT)
                        .requestMatchers("/api/officier/**").hasRole(OFFICIER)
                        .requestMatchers("/api/citoyen/**").hasRole(CITOYEN)
                        .requestMatchers("/api/user/**").hasAnyRole(CITOYEN, ADMINSYSTEME,ADMINMAIRIE,AGENT,OFFICIER)
                        .anyRequest().authenticated());

        http.oauth2ResourceServer(rsc->rsc.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        return new HttpHeaders();
    }
}
