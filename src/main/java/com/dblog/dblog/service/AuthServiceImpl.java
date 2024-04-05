package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.repo.UserRepo;
import com.dblog.dblog.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;



    @Override
    public String authenticateUser(User user){
        User storedUser = userRepo.findByUser(user.getUser());
        if (storedUser == null ) throw new BadCredentialsException("Credenciales inválidas");
        if (!storedUser.isEmailValidated()){
            return "Email no validado.";
        }
        if (!storedUser.isValidated()) {
            return "El usuario no está validado. Por favor, espere a que su cuenta sea validada por un administrador.";
        }
        if (!passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", storedUser.getId());
        claims.put("username", storedUser.getUser());
        if (storedUser.isAdmin()) {
            claims.put("role", "ADMIN");
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(storedUser.getUser())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtTokenProvider.getKey())
                .compact();
    }

    @Override
    public boolean validateJwtToken(String token) {
        return jwtTokenProvider.validateJwtToken(token);
    }

    @Override
    public Authentication getAuthentication(String token) {
        return jwtTokenProvider.getAuthentication(token);
    }
}
