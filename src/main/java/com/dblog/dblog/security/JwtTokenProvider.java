package com.dblog.dblog.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class JwtTokenProvider {


    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        // Utiliza la clave proporcionada en la configuración
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    public Key getKey() {
        return this.key;
    }
    public boolean validateJwtToken(String token) {
        try {
            System.out.println("Validating token: " + token);

            // Imprime la clave
            System.out.println("Key: " + Arrays.toString(key.getEncoded()));
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            System.err.println("Exception: " + ex.getMessage());
            return false;
        }

    }

    public Authentication getAuthentication(String token) {
        String username = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
}
