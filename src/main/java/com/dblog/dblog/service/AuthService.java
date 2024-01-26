package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    String authenticateUser(User user);
    boolean validateJwtToken(String token);
    Authentication getAuthentication(String token);
}
