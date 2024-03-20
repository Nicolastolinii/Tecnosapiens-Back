package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.repo.UserRepo;
import com.dblog.dblog.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;



    @Override
    public String authenticateUser(User user){
        User storedUser = userRepo.findByUserAndPassword(user.getUser(), user.getPassword());
        if (storedUser == null) throw new BadCredentialsException("Credenciales inválidas");
        String token = Jwts.builder()
                .setSubject(storedUser.getUser())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtTokenProvider.getKey())
                .compact();
        return token;
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
