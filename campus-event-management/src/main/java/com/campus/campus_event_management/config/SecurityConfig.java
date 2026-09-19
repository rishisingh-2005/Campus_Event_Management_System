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

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
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

                // CORS preflight requests
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                )
                .permitAll()

                // Public student registration
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users"
                )
                .permitAll()

                // Public login
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users/login"
                )
                .permitAll()

                // Only ADMIN can view all users
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users"
                )
                .hasRole("ADMIN")

                // ADMIN and ORGANIZER can view a user
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/{id}"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // ADMIN and ORGANIZER can find user by email
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/email/{email}"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Only ADMIN can delete users
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/users/{id}"
                )
                .hasRole("ADMIN")

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

                // Direct attendance APIs
                // Only ADMIN can access these endpoints
                .requestMatchers(
                    "/api/attendance/**"
                )
                .hasRole("ADMIN")

                // Certificate generation
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/certificates"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Students can view their own certificates
                // Controller checks ownership
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/user/**"
                )
                .hasRole("STUDENT")

                // Students, ADMIN and ORGANIZER can reach
                // certificate-by-ID endpoint.
                // Controller performs ownership checks.
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/{id}"
                )
                .hasAnyRole(
                    "STUDENT",
                    "ADMIN",
                    "ORGANIZER"
                )

                // Other certificate GET endpoints
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Student-specific APIs
                .requestMatchers(
                    "/api/student/**"
                )
                .hasRole("STUDENT")

                // Students can create registrations
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/registrations"
                )
                .hasRole("STUDENT")

                // Students can view their own registrations
                // Controller checks ownership
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/user/**"
                )
                .hasRole("STUDENT")

                // Only ADMIN and ORGANIZER can view
                // registrations for an event
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/event/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // Students can delete their own registrations
                // Controller checks ownership
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/registrations/*"
                )
                .hasRole("STUDENT")

                // Students can cancel their own registrations
                // Controller checks ownership
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/registrations/*/cancel"
                )
                .hasRole("STUDENT")

                // Everything else requires authentication
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