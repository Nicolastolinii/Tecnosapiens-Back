package com.dblog.dblog.service;

import com.dblog.dblog.model.User;

public interface AuthService {
    String authenticateUser(User user);
}
