package com.dblog.dblog.controller;

import com.dblog.dblog.model.dtos.UserDto;
import com.dblog.dblog.service.UserService;
import com.dblog.dblog.utils.LogDuration;
import com.dblog.dblog.utils.Logger;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/v1")
@MultipartConfig
//@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"},allowCredentials = "true")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = new Logger();
    private static final String IMAGE_DIR = "/root/app/image/userimg/";

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        long start = System.currentTimeMillis();
        UserDto user = userService.getUserById(userId);
        LogDuration.logDuration("getUser()", Duration.ofMillis(System.currentTimeMillis()-start), user.getBlogs().size());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        long start = System.currentTimeMillis();
        List<UserDto> user = userService.getAllUsers();
        LogDuration.logDuration("getAllUsers()", Duration.ofMillis(System.currentTimeMillis()-start), user.size());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(IMAGE_DIR + fileName);
            File file = imagePath.toFile();
            if (file.exists()) {
                Resource resource = new UrlResource(file.toURI());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
