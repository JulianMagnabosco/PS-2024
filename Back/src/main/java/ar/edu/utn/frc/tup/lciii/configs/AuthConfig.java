package ar.edu.utn.frc.tup.lciii.configs;

import ar.edu.utn.frc.tup.lciii.configs.auth.SecurityFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class AuthConfig {
  @Autowired
  SecurityFilter securityFilter;

//  @Override
//  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//                       FilterChain filterChain) throws IOException, ServletException {
//    HttpServletResponse response = (HttpServletResponse) servletResponse;
//    HttpServletRequest request = (HttpServletRequest) servletRequest;
//    response.setHeader("Access-Control-Allow-Origin", "*");
//    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
//    response.setHeader("Access-Control-Max-Age", "3600");
//    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization, Content-Type");
//
//    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//      response.setStatus(HttpServletResponse.SC_OK);
//    } else {
//      filterChain.doFilter(request, response);
//    }
//  }
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/pub/**").permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}