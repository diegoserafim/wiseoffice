package com.example.webapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        // Verifica se é uma requisição AJAX/API
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        
        if ("XMLHttpRequest".equals(requestedWith) || 
            (accept != null && accept.contains("application/json"))) {
            // Para requisições AJAX/API, retorna 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
                "Acesso negado. Token de autenticação necessário.");
        } else {
            // Para requisições web normais, redireciona para login
            response.sendRedirect("/login?error=unauthorized");
        }
    }
}

