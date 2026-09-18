package com.campus.campus_event_management.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.campus.campus_event_management.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Show which request is reaching the JWT filter
        System.out.println(
                "JWT Filter Request: "
                + request.getMethod()
                + " "
                + request.getRequestURI()
        );

        String authHeader = request.getHeader("Authorization");

        System.out.println(
                "Authorization Header: " + authHeader
        );

        // No JWT token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "No Bearer token found."
            );

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // Remove "Bearer "
            String token = authHeader.substring(7);

            // Extract email
            String email = jwtService.extractEmail(token);

            // Extract role
            String role = jwtService
                    .extractClaims(token)
                    .get("role", String.class);

            System.out.println(
                    "JWT Email: " + email
            );

            System.out.println(
                    "JWT Role: " + role
            );

            if (email != null && role != null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "JWT Authentication successful."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Authentication Error: "
                    + e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}