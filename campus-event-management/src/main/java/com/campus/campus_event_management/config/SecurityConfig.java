package com.campus.campus_event_management.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.campus.campus_event_management.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors ->
                cors.configurationSource(corsConfigurationSource())
            )

            .csrf(csrf ->
                csrf.disable()
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Browser preflight
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                )
                .permitAll()

                // User / Login APIs
                .requestMatchers(
                    "/api/users/**"
                )
                .permitAll()

                // Admin APIs
                .requestMatchers(
                    "/api/admin/**"
                )
                .hasRole("ADMIN")

                // Organizer APIs
                .requestMatchers(
                    "/api/organizer/**"
                )
                .hasRole("ORGANIZER")

                // Attendance
                .requestMatchers(
                    "/api/attendance"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Certificate generation
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/certificates"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Student certificate list
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/user/**"
                )
                .hasRole("STUDENT")

                // Student individual certificate
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/{id}"
                )
                .hasRole("STUDENT")

                // Admin / Organizer certificate access
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Student APIs
                .requestMatchers(
                    "/api/student/**"
                )
                .hasRole("STUDENT")

                // Student registration APIs
                .requestMatchers(
                    "/api/registrations/**"
                )
                .hasRole("STUDENT")

                // Everything else
                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of(
                "http://localhost:5500",
                "http://127.0.0.1:5500"
            )
        );

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}