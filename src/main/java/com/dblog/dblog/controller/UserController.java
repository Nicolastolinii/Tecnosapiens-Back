package com.dblog.dblog.controller;

import com.dblog.dblog.model.User;
import com.dblog.dblog.model.dtos.UserDto;
import com.dblog.dblog.service.UserService;
import com.dblog.dblog.utils.EmailUtil;
import com.dblog.dblog.utils.GenerateOtp;
import com.dblog.dblog.utils.LogDuration;
import com.dblog.dblog.utils.Logger;
import jakarta.mail.MessagingException;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1")
@MultipartConfig
//@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"})
//@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GenerateOtp generateOtp;
    @Autowired
    private EmailUtil emailUtil;
    private static final Logger logger = new Logger();
    private static final String IMAGE_DIR = "/root/app/image/userimg/";
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/data/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        long start = System.currentTimeMillis();
        UserDto user = userService.getUserById(userId);
        LogDuration.logDuration("getUser()", Duration.ofMillis(System.currentTimeMillis()-start), user.getBlogs().size());
        return ResponseEntity.ok(user);
    }

    @PostMapping(value="/users/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity<Void> uploadImage (@RequestParam("file") MultipartFile file,
                                              @RequestParam("autorId") Long autorId) throws Exception {
        User user = userService.userById(autorId);
        String imageData = userService.uploadImage(file,autorId);
        String imageUrl = "https://api.tecnosapiens.blog" + "/v1/user/" + FilenameUtils.getName(imageData);
        user.setImage(imageUrl);
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }
    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user){
        List<String> emails = userService.findAllEmails();
        List<String> userName = userService.findAllUserName();
        if (emails.contains(user.getCorreo())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (userName.contains(user.getUser())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (user.getUser() == null || user.getUser().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String otp = generateOtp.generate();
        try {
            emailUtil.sendOtpEmail(user.getCorreo(), otp);
        }catch (Exception e){
            throw new RuntimeException("No se pudo enviar el OTP, intentelo de nuevo.", e);
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setPassword(encodedPassword);
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        long start = System.currentTimeMillis();
        List<UserDto> user = userService.getAllUsers();
        LogDuration.logDuration("getAllUsers()", Duration.ofMillis(System.currentTimeMillis()-start), user.size());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{fileName}")
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
