package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MairieExpress")
                        .version("1.0")
                        .description("Récuperation des documents etat civil chez vous"));
    }
}
