package com.example.demo.security;

import lombok.Data;

@Data
public class AuthenticationDto {
    public String token;
    private String tokenType = "Bearer ";
    public AuthenticationDto(String token) {
        this.token = token;
    }

}
