package com.Non_academicWebsite.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.Non_academicWebsite.Entity.Permission.*;
import static com.Non_academicWebsite.Entity.Role.ADMIN;
import static com.Non_academicWebsite.Entity.Role.MANAGER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("api/auth/**").permitAll();

                    auth.requestMatchers("api/admin/**").hasRole(ADMIN.name());
                    auth.requestMatchers(HttpMethod.GET,"api/admin/**").hasAuthority(ADMIN_READ.name());
                    auth.requestMatchers(HttpMethod.POST,"api/admin/**").hasAuthority(ADMIN_CREATE.name());
                    auth.requestMatchers(HttpMethod.PUT,"api/admin/**").hasAuthority(ADMIN_UPDATE.name());
                    auth.requestMatchers(HttpMethod.DELETE,"api/admin/**").hasAuthority(ADMIN_DELETE.name());

                    auth.requestMatchers("/api/management/**").hasAnyRole(ADMIN.name(), MANAGER.name());
                    auth.requestMatchers(HttpMethod.GET,"/api/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name());
                    auth.requestMatchers(HttpMethod.POST,"/api/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name());
                    auth.requestMatchers(HttpMethod.PUT,"/api/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name());
                    auth.requestMatchers(HttpMethod.DELETE,"/api/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name());
                    auth.anyRequest().authenticated();
                });
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
