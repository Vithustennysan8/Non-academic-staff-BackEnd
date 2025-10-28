package com.Non_academicWebsite.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                        .requestMatchers("api/v1/auth/**").permitAll()

                        .requestMatchers("api/v1/user/**").
                            hasAnyRole(USER.name(), MANAGER.name(), ADMIN.name(), SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**").hasAnyAuthority(USER.name(), ADMIN_READ.name(),
                                    SUPER_ADMIN_READ.name(), MANAGER_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/**").hasAnyAuthority(USER.name(), ADMIN_CREATE.name(),
                                    SUPER_ADMIN_CREATE.name(), MANAGER_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAnyAuthority(USER.name(), ADMIN_UPDATE.name(),
                                    SUPER_ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyAuthority(USER.name(), ADMIN_DELETE.name(),
                                    SUPER_ADMIN_DELETE.name(), MANAGER_DELETE.name())

                        .requestMatchers("api/v1/super_admin/**").hasRole(SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "api/v1/super_admin/**").hasAuthority(SUPER_ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "api/v1/super_admin/**").hasAuthority(SUPER_ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "api/v1/super_admin/**").hasAuthority(SUPER_ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "api/v1/super_admin/**").hasAuthority(SUPER_ADMIN_DELETE.name())

                        .requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAnyAuthority(ADMIN_READ.name(),
                                    SUPER_ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAnyAuthority(ADMIN_CREATE.name(),
                                    SUPER_ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAnyAuthority(ADMIN_UPDATE.name(),
                                    SUPER_ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAnyAuthority(ADMIN_DELETE.name(),
                                    SUPER_ADMIN_DELETE.name()).requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())

                        .requestMatchers("/api/v1/manager/**").hasAnyRole(ADMIN.name(), SUPER_ADMIN.name(),
                                    MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/manager/**").hasAnyAuthority(ADMIN_READ.name(),
                                    SUPER_ADMIN_READ.name(), MANAGER_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/manager/**").hasAnyAuthority(ADMIN_CREATE.name(),
                                    SUPER_ADMIN_CREATE.name(), MANAGER_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(),
                                    SUPER_ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/manager/**").hasAnyAuthority(ADMIN_DELETE.name(),
                                    SUPER_ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
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
