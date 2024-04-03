package ar.edu.utn.frc.tup.lciii.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String[] allowDomains = new String[2];
        allowDomains[0] = "http://localhost:4200";
        allowDomains[1] = "http://localhost:8080";
        String[] allowMethods = new String[4];
        allowMethods[0] = HttpMethod.GET.name();
        allowMethods[1] = HttpMethod.POST.name();
        allowMethods[2] = HttpMethod.PUT.name();
        allowMethods[3] = HttpMethod.DELETE.name();

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
//                    .allowedOrigins(allowDomains)
//                        .allowCredentials(true)
                        .allowedOrigins("*")
                        .allowedMethods(allowMethods)
                        .allowedHeaders("*");
//                    .allowedHeaders(HttpHeaders.CONTENT_TYPE,
//                            HttpHeaders.AUTHORIZATION);
            }
        };
    }

}
