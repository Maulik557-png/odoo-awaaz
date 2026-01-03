package com.odoo.hackathon.hrms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            // Log token summary for debugging (do not log full token in production)
            try {
                String[] parts = token.split("\\.");
                log.debug("Incoming token parts lengths: header={}, payload={}, signature={}", parts.length>0?parts[0].length():0, parts.length>1?parts[1].length():0, parts.length>2?parts[2].length():0);
            } catch (Exception e) {
                log.debug("Unable to parse token parts for logging");
            }
            // Support a simple dev token format: devtoken-<base64(login:role)>
            if (token.startsWith("devtoken-")) {
                try {
                    String b = token.substring("devtoken-".length());
                    String decoded = new String(Base64.getUrlDecoder().decode(b));
                    String[] parts = decoded.split(":", 2);
                    String loginId = parts[0];
                    String role = parts.length > 1 ? parts[1] : null;
                    SimpleGrantedAuthority authority = role == null ? null : new SimpleGrantedAuthority("ROLE_" + role);
                    UsernamePasswordAuthenticationToken auth = authority == null
                            ? new UsernamePasswordAuthenticationToken(loginId, null, Collections.emptyList())
                            : new UsernamePasswordAuthenticationToken(loginId, null, Collections.singletonList(authority));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception ex) {
                    log.error("Failed to parse dev token", ex);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                filterChain.doFilter(request, response);
                return;
            }
            if (!provider.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // Extract user info from token and set authentication
            String loginId = provider.getLoginIdFromToken(token);
            String role = provider.getRoleFromToken(token);
            SimpleGrantedAuthority authority = role == null ? null : new SimpleGrantedAuthority("ROLE_" + role);
            UsernamePasswordAuthenticationToken auth = authority == null
                    ? new UsernamePasswordAuthenticationToken(loginId, null, Collections.emptyList())
                    : new UsernamePasswordAuthenticationToken(loginId, null, Collections.singletonList(authority));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}