package com.diplomado.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwtToken = getJwtFromRequest(request);

        if (jwtToken != null) {
            System.out.println("Token recibido: " + jwtToken);
        }

        if (jwtToken != null && tokenProvider.validateToken(jwtToken)) {
            String username = tokenProvider.getUsernameFromToken(jwtToken);
            System.out.println("Usuario extraído del token: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null) {
                System.out.println("Usuario encontrado: " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("No se pudo encontrar el usuario");
            }
        } else {
            System.out.println("Token inválido o no encontrado");
        }

        chain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Verificar si el encabezado contiene el token y comienza con "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extraer el token, eliminando "Bearer "
        }
        return null;
    }
}
