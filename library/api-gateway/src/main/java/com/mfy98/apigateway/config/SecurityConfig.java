package com.mfy98.apigateway.config;

import com.mfy98.apigateway.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                        .pathMatchers("/swagger-ui/**", "/webjars/**").permitAll()
                        .pathMatchers("/actuator/health").permitAll()

                        .pathMatchers(HttpMethod.POST,   "/books").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.PUT,    "/books/**").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.PATCH,  "/books/**").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("LIBRARIAN")

                        .pathMatchers(HttpMethod.POST,   "/users").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.PUT,    "/users/**").hasAuthority("LIBRARIAN")
                        .pathMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("LIBRARIAN")

                        .pathMatchers(HttpMethod.POST,  "/borrow").hasAuthority("PATRON")
                        .pathMatchers(HttpMethod.PUT,   "/borrow/return/**").hasAuthority("PATRON")
                        .pathMatchers(HttpMethod.GET, "/reports/overdue").hasAuthority("LIBRARIAN")

                        .pathMatchers(HttpMethod.GET, "/books/**").hasAnyAuthority("LIBRARIAN","PATRON")
                        .pathMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("LIBRARIAN","PATRON")
                        .pathMatchers(HttpMethod.GET, "/borrow/**").hasAnyAuthority("LIBRARIAN","PATRON")

                        .anyExchange().authenticated()
                )

                .addFilterAt((WebFilter) jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
