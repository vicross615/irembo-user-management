package com.irembo.useraccountmanagement.dto;

/**
 * Created by USER on 5/2/2023.
 */
public class JwtAuthenticationResponse {

    private String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}