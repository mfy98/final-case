/*
package com.mfy98.bookservice.config;

import com.mfy98.bookservice.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain webFluxSecurity(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex

                        .pathMatchers(HttpMethod.POST,   "/books").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.PUT,    "/books/**").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.PATCH,  "/books/**").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("LIBRARIAN")

                        .pathMatchers(HttpMethod.GET, "/books/**").hasAnyAuthority("LIBRARIAN","PATRON")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
*/
