package com.vp.applifting.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final AuthenticationTokenFilter authenticationTokenFilter;


  public SecurityConfig(AuthenticationTokenFilter authenticationTokenFilter) {
    this.authenticationTokenFilter = authenticationTokenFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/api/endpoints").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/endpoints").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/endpoints/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/endpoints/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/results/**").authenticated()
            .anyRequest().authenticated())
        .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
