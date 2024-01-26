package com.dblog.dblog.service;

import com.dblog.dblog.model.User;
import com.dblog.dblog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepo userRepo;


    @Override
    public String authenticateUser(User user){
        User storedUser = userRepo.findByUserAndPassword(user.getuser(), user.getpassword());
        if (storedUser == null) throw new RuntimeException("Credenciales inválidas");
        return "login exitoso";
    }
}
