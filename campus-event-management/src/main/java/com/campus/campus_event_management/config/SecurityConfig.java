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

                // =========================
                // CORS
                // =========================

                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                )
                .permitAll()

                // =========================
                // PUBLIC USER REGISTRATION
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users"
                )
                .permitAll()

                // =========================
                // PUBLIC LOGIN
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users/login"
                )
                .permitAll()

                // =========================
                // ORGANIZER REQUESTS
                // =========================

                // Anyone can request an organizer account
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/organizer-requests"
                )
                .permitAll()

                // Only ADMIN can view pending requests
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/organizer-requests/pending"
                )
                .hasRole("ADMIN")

                // Only ADMIN can approve
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/organizer-requests/*/approve"
                )
                .hasRole("ADMIN")

                // Only ADMIN can reject
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/organizer-requests/*/reject"
                )
                .hasRole("ADMIN")

                // =========================
                // USER MANAGEMENT
                // =========================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users"
                )
                .hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/{id}"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/email/{email}"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/users/{id}"
                )
                .hasRole("ADMIN")

                // =========================
                // ADMIN APIs
                // =========================

                .requestMatchers(
                    "/api/admin/**"
                )
                .hasRole("ADMIN")

                // =========================
                // ORGANIZER APIs
                // =========================

                .requestMatchers(
                    "/api/organizer/**"
                )
                .hasRole("ORGANIZER")

                // =========================
                // ATTENDANCE
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendance"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance/event/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance/user/**"
                )
                .hasAnyRole(
                    "STUDENT",
                    "ADMIN",
                    "ORGANIZER"
                )

                // =========================
                // CERTIFICATES
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/certificates"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/user/**"
                )
                .hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/{id}"
                )
                .hasAnyRole(
                    "STUDENT",
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                // =========================
                // STUDENT APIs
                // =========================

                .requestMatchers(
                    "/api/student/**"
                )
                .hasRole("STUDENT")

                // =========================
                // REGISTRATIONS
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/registrations"
                )
                .hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/user/**"
                )
                .hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/event/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/registrations/*"
                )
                .hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/registrations/*/cancel"
                )
                .hasRole("STUDENT")

                // =========================
                // EVERYTHING ELSE
                // =========================

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