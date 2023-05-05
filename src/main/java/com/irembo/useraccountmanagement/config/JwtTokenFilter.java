package com.irembo.useraccountmanagement.config;

import com.irembo.useraccountmanagement.entity.User;
import com.irembo.useraccountmanagement.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

/**
 * Created by USER on 5/2/2023.
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final UserDetailsService userDetailsService;
    private final String jwtSecret;
    private final long jwtExpiration;

    private final SecretKey secretKey;

    public JwtTokenFilter(UserDetailsService userDetailsService, String jwtSecret, long jwtExpiration) {
        this.userDetailsService = userDetailsService;
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getTokenFromRequest(request);

        if (jwtToken != null) {

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null && isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) {
                // log exception
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        JwtUserPrincipal userPrincipal = (JwtUserPrincipal) userDetails;

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.after(new Date()) && userPrincipal.getId().equals(getUserIdFromToken(token));
    }

    private String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", String.class);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTH_HEADER_NAME);
        if (headerValue != null && headerValue.startsWith(AUTH_HEADER_PREFIX)) {
            return headerValue.substring(AUTH_HEADER_PREFIX.length());
        }
        return null;
    }

    private UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}