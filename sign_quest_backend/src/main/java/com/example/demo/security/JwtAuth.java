package com.example.demo.security;

import java.io.IOException;

import javax.naming.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuth implements AuthenticationEntryPoint {
     
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        // TODO Auto-generated method stub
    }
}
