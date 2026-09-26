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

                /* ==============================
                   OPTIONS / CORS
                ============================== */

                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()


                /* ==============================
                   USER REGISTRATION
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users"
                ).permitAll()


                /* ==============================
                   LOGIN
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users/login"
                ).permitAll()


                /* ==============================
                   ORGANIZER REGISTRATION REQUEST
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/organizer-requests"
                ).permitAll()


                /* ==============================
                   PUBLIC UPCOMING EVENTS
                ============================== */

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/public/events"
                ).permitAll()


                /* ==============================
                   ADMIN ORGANIZER REQUESTS
                ============================== */

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/organizer-requests/pending"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/organizer-requests/*/approve"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/organizer-requests/*/reject"
                ).hasRole("ADMIN")


                /* ==============================
                   ADMIN USER MANAGEMENT
                ============================== */

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/{id}"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/users/email/{email}"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/users/{id}"
                ).hasRole("ADMIN")


                /* ==============================
                   ADMIN APIs
                ============================== */

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")


                /* ==============================
                   ORGANIZER APIs
                ============================== */

                .requestMatchers(
                    "/api/organizer/**"
                ).hasRole("ORGANIZER")


                /* ==============================
                   ATTENDANCE
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendance"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance/event/**"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance/user/**"
                ).hasAnyRole(
                    "STUDENT",
                    "ADMIN",
                    "ORGANIZER"
                )


                /* ==============================
                   CERTIFICATES
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/certificates"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/certificates/event/*/generate-all"
                ).hasAnyRole("ADMIN", "ORGANIZER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/user/**"
                ).hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/{id}"
                ).hasAnyRole(
                    "STUDENT",
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/certificates/**"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )


                /* ==============================
                   STUDENT APIs
                ============================== */

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/student/events"
                    ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/student/**"
                ).hasRole("STUDENT")


                /* ==============================
                   REGISTRATIONS
                ============================== */

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/registrations"
                ).hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/user/**"
                ).hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/registrations/event/**"
                ).hasAnyRole(
                    "ADMIN",
                    "ORGANIZER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/registrations/*"
                ).hasRole("STUDENT")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/registrations/*/cancel"
                ).hasRole("STUDENT")


                /* ==============================
                   ALL OTHER REQUESTS
                ============================== */

                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }


    /* ==============================
       PASSWORD ENCODER
    ============================== */

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    /* ==============================
       CORS CONFIGURATION
    ============================== */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
    List.of(
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "https://whimsical-valkyrie-76a919.netlify.app"
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