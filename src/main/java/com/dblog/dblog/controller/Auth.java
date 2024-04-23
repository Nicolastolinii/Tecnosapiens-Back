package com.dblog.dblog.controller;

import com.dblog.dblog.model.User;
import com.dblog.dblog.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"})
//@CrossOrigin(origins = "*")

@AllArgsConstructor
public class Auth {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            String response = authService.authenticateUser(user);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }else if (response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }

    }


}
