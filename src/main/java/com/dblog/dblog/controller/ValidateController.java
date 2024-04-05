package com.dblog.dblog.controller;


import com.dblog.dblog.model.User;
import com.dblog.dblog.service.UserService;
import com.dblog.dblog.utils.Logger;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
@MultipartConfig
//@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"},allowCredentials = "true")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ValidateController {

    @Autowired
    private UserService userService;

    private static final Logger logger = new Logger();

    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,@RequestParam String otp){
        return new ResponseEntity<>(userService.verifyAccount(email, otp),HttpStatus.OK);
    }

    @PutMapping("/user-validate/{id}/acc")
    public ResponseEntity<String> validateUser(@PathVariable Long id) {
        try {
            userService.validateUser(id);
            return ResponseEntity.ok("Usuario validado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo validar al usuario: " + e.getMessage());
        }
    }


}
