package com.dblog.dblog.controller;

import com.dblog.dblog.model.User;
import com.dblog.dblog.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class Auth {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String response = authService.authenticateUser(user);
        return ResponseEntity.ok(response);
    }



}
