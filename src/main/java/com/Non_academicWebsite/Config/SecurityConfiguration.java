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
import static com.Non_academicWebsite.Entity.Role.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {auth
                        .requestMatchers("api/auth/**").permitAll()

                        .requestMatchers("api/super_admin/**").hasRole(SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "api/super_admin/**").hasAuthority(SUPER_ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "api/super_admin/**").hasAuthority(SUPER_ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "api/super_admin/**").hasAuthority(SUPER_ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "api/super_admin/**").hasAuthority(SUPER_ADMIN_DELETE.name())

                        .requestMatchers("/api/admin/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAnyAuthority(ADMIN_READ.name(), SUPER_ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasAnyAuthority(ADMIN_CREATE.name(), SUPER_ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasAnyAuthority(ADMIN_UPDATE.name(), SUPER_ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasAnyAuthority(ADMIN_DELETE.name(), SUPER_ADMIN_DELETE.name()).requestMatchers("/api/admin/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())

                        .requestMatchers("/api/admin/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name(), MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/manager/**").hasAnyAuthority(ADMIN_READ.name(), SUPER_ADMIN_READ.name(), MANAGER_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/manager/**").hasAnyAuthority(ADMIN_CREATE.name(), SUPER_ADMIN_CREATE.name(), MANAGER_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(), SUPER_ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/manager/**").hasAnyAuthority(ADMIN_DELETE.name(), SUPER_ADMIN_DELETE.name(), MANAGER_DELETE.name())


                        .anyRequest().authenticated();
                });
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
