package com.odoo.hackathon.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.odoo.hackathon.hrms.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .cors() // enable CORS support (configured by CorsFilter bean)
            .and()
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/add").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // static resources for the frontend
                .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
            );
        // Ensure our JWT filter runs before the standard username/password filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
	}
}